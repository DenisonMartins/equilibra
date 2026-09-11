package br.com.mmtech.equilibra.backend.autenticacao.controller;

import br.com.mmtech.equilibra.backend.config.seguranca.JwtService;
import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarTokenResponse;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import br.com.mmtech.equilibra.backend.login.dto.LoginRequest;
import br.com.mmtech.equilibra.backend.login.dto.LoginResponse;
import br.com.mmtech.equilibra.backend.usuario.entity.Usuario;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/autenticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final ConviteService conviteService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @GetMapping("validar-token")
    public ValidarTokenResponse validarToken(@RequestParam String token) {
        return conviteService.validarToken(token);
    }

    @PostMapping("aceitar-convite")
    public void aceitarConvite(@RequestBody @Valid AceiteConviteRequest dto) {
        conviteService.aceitarConvite(dto);
    }

    @PostMapping("login")
    public LoginResponse login(@RequestBody @Valid LoginRequest dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email().toLowerCase(Locale.ROOT).trim())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!passwordEncoder.matches(dto.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        if (!usuario.isAtivo()) {
            throw new BadCredentialsException("Usuário inativo");
        }

        String token = jwtService.gerarToken(usuario);
        return new LoginResponse(token, usuario.getNome(), usuario.getEmail(), usuario.getPerfil().name());
    }
}
