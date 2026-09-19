package br.com.mmtech.equilibra.backend.convite.service.impl;

import br.com.mmtech.equilibra.backend.comum.exception.EntidadeNaoEncontradaException;
import br.com.mmtech.equilibra.backend.convite.domain.model.Convite;
import br.com.mmtech.equilibra.backend.convite.exception.ConviteInvalidoException;
import br.com.mmtech.equilibra.backend.convite.repository.ConviteRepository;
import br.com.mmtech.equilibra.backend.convite.repository.specifications.ConviteSpecs;
import br.com.mmtech.equilibra.backend.email.service.EmailService;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Perfil;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import br.com.mmtech.equilibra.backend.usuario.exception.UsuarioCadastradoException;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import br.com.mmtech.equilibra.backend.convite.domain.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.domain.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.domain.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.domain.dto.ValidarConviteResponse;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConviteServiceImpl implements ConviteService {

    private final UsuarioRepository usuarioRespository;
    private final ConviteRepository repository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ConviteResponse criar(String email) {
        if (usuarioRespository.existsByEmail(email)) {
            throw new UsuarioCadastradoException("Já existe um usuário cadastrado com esse email.");
        }

        String hash = UUID.randomUUID().toString().replace("-","");

        Convite convite = repository.save(Convite.builder()
                .email(email)
                .hash(hash)
                .expiraEm(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(48))
                .utilizado(false)
                .build());
        log.info("Convite criado com sucesso. ID {}, email {}", convite.getId(), convite.getEmail());

        emailService.enviarEmailConvite(convite.getEmail(), convite.getHash());
        return ConviteResponse.of(convite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConviteResponse> listar(ConviteFilter conviteFilter, Pageable pageable) {
        Specification<Convite> specification = ConviteSpecs.comFiltro(conviteFilter);
        return repository.findAll(specification, pageable).map(ConviteResponse::of);
    }

    @Override
    @Transactional(readOnly = true)
    public ValidarConviteResponse validarHash(String hash) {
        return repository.findByHash(hash)
                .filter(convite -> !convite.isUtilizado() && convite.getExpiraEm().isAfter(LocalDateTime.now(ZoneId.of("America/Sao_Paulo"))))
                .map(convite -> new ValidarConviteResponse(true, convite.getEmail()))
                .orElse(new ValidarConviteResponse(false, null));
    }

    @Override
    public void aceitarConvite(AceiteConviteRequest aceiteConviteRequest) {
        Convite convite = repository.findByHash(aceiteConviteRequest.hash())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Hash de convite não localizado"));

        validarConviteUtilizadoOuExpirado(convite);
        validarUsuarioExistente(convite);

        Usuario usuario = Usuario.builder()
                .nome(aceiteConviteRequest.nome())
                .email(convite.getEmail())
                .senha(passwordEncoder.encode(aceiteConviteRequest.senha()))
                .perfil(Perfil.ROLE_USER)
                .ativo(true)
                .build();

        usuarioRespository.save(usuario);
        convite.setUtilizado(true);
        repository.save(convite);

        log.info("Conta ativada com sucesso para o usuário {}", usuario.getEmail());
    }

    private void validarUsuarioExistente(Convite convite) {
        if (usuarioRespository.existsByEmail(convite.getEmail())) {
            throw new UsuarioCadastradoException("Usuário já cadastrado");
        }
    }

    private static void validarConviteUtilizadoOuExpirado(Convite convite) {
        if (convite.isUtilizado()) {
            throw new ConviteInvalidoException("Este convite já foi utilizado");
        }

        if (convite.getExpiraEm().isBefore(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))) {
            throw new ConviteInvalidoException("Este convite está expirado.");
        }
    }
}
