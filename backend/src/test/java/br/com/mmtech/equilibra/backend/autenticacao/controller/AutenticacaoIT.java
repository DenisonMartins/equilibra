package br.com.mmtech.equilibra.backend.autenticacao.controller;

import br.com.mmtech.equilibra.backend.AbstractIntegrationTest;
import br.com.mmtech.equilibra.backend.autenticacao.dto.LoginRequest;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Perfil;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import br.com.mmtech.equilibra.backend.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static br.com.mmtech.equilibra.backend.usuario.domain.model.Perfil.ROLE_ADMIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AutenticacaoIT extends AbstractIntegrationTest {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve autenticar com credenciais corretas e retornar token JWT")
    void deveAutenticarComSucesso() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("Admin Teste")
                .email("admin.teste@email.com")
                .senha(passwordEncoder.encode("SenhaForte@123"))
                .perfil(ROLE_ADMIN)
                .ativo(true)
                .build();
        usuarioRepository.save(usuario);

        LoginRequest loginRequest = new LoginRequest("admin.teste@email.com", "SenhaForte@123");
        mockMvc.perform(post("/autenticacao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.email").value("admin.teste@email.com"))
                .andExpect(jsonPath("$.perfil").value("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("Deve rejeitar credenciais com senha incorreta")
    void deveRejeitarSenhaInvalida() throws Exception {
        Usuario usuario = Usuario.builder()
                .nome("Usuario Teste")
                .email("user.teste@equilibra.com.br")
                .senha(passwordEncoder.encode("SenhaForte@123"))
                .perfil(Perfil.ROLE_USER)
                .ativo(true)
                .build();
        usuarioRepository.save(usuario);

        LoginRequest loginRequest = new LoginRequest("user.teste@equilibra.com.br", "SenhaErrada");
        mockMvc.perform(post("/autenticacao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}