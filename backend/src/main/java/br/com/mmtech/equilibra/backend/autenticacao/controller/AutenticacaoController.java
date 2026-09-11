package br.com.mmtech.equilibra.backend.autenticacao.controller;

import br.com.mmtech.equilibra.backend.autenticacao.dto.LoginRequest;
import br.com.mmtech.equilibra.backend.autenticacao.dto.LoginResponse;
import br.com.mmtech.equilibra.backend.config.seguranca.JwtService;
import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarConviteResponse;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import br.com.mmtech.equilibra.backend.usuario.entity.Usuario;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/autenticacao")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoint para autenticação, ativação de convite e validação de convites")
public class AutenticacaoController {

    private final ConviteService conviteService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Operation(summary = "Validar hash de convite",
            description = "Verifica se o hash de convite informado na URL ainda é válido e não expirou.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado da validação do hash")
    })
    @GetMapping("/validar-hash")
    public ValidarConviteResponse validarHash(@RequestParam String hash) {
        return conviteService.validarHash(hash);
    }

    @Operation(summary = "Aceitar convite e cadastrar senha",
            description = "Consome o convite e cria a conta do novo usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Conta ativada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Convite expirado, hash inválido ou dados incorretos")
    })
    @PostMapping("/aceitar-convite")
    public void aceitarConvite(@RequestBody @Valid AceiteConviteRequest dto) {
        conviteService.aceitarConvite(dto);
    }

    @Operation(summary = "Realizar login no sistema",
            description = "Autentica usuário com e-mail e senha e retorna o token JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
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
        return new LoginResponse(token, usuario.getNome(), usuario.getEmail(), usuario.getPerfil());
    }
}
