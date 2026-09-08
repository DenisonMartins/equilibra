package br.com.mmtech.equilibra.backend.convite.service.impl;

import br.com.mmtech.equilibra.backend.convite.entity.Convite;
import br.com.mmtech.equilibra.backend.convite.repository.ConviteRepository;
import br.com.mmtech.equilibra.backend.convite.repository.specifications.ConviteSpecification;
import br.com.mmtech.equilibra.backend.email.service.EmailService;
import br.com.mmtech.equilibra.backend.usuario.entity.Perfil;
import br.com.mmtech.equilibra.backend.usuario.entity.Usuario;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarTokenResponse;
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
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse email.");
        }

        String token = UUID.randomUUID().toString().replace("-","");

        Convite convite = repository.save(Convite.builder()
                .email(email)
                .token(token)
                .expiraEm(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(48))
                .utilizado(false)
                .build());
        log.info("Convite criado com sucesso. ID {}, email {}", convite.getId(), convite.getEmail());

        emailService.enviarEmailConvite(convite.getEmail(), convite.getToken());
        return ConviteResponse.of(convite);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConviteResponse> listar(ConviteFilter conviteFilter, Pageable pageable) {
        Specification<Convite> specification = ConviteSpecification.comFiltro(conviteFilter);
        return repository.findAll(specification, pageable).map(ConviteResponse::of);
    }

    @Override
    @Transactional(readOnly = true)
    public ValidarTokenResponse validarToken(String token) {
        return repository.findByToken(token)
                .filter(convite -> !convite.isUtilizado() && convite.getExpiraEm().isAfter(LocalDateTime.now()))
                .map(convite -> new ValidarTokenResponse(true, convite.getEmail()))
                .orElse(new ValidarTokenResponse(false, null));
    }

    @Override
    public void aceitarConvite(AceiteConviteRequest aceiteConviteRequest) {
        Convite convite = repository.findByToken(aceiteConviteRequest.token())
                .orElseThrow(() -> new IllegalArgumentException("Token de convite não localizado"));

        if (convite.isUtilizado()) {
            throw new IllegalArgumentException("Este convite já foi utilizado");
        }

        if (convite.getExpiraEm().isBefore(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))) {
            throw new IllegalArgumentException("Este convite está expirado.");
        }

        if (usuarioRespository.existsByEmail(convite.getEmail())) {
            throw new IllegalArgumentException("Usuário já cadastrado");
        }

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
}
