package br.com.mmtech.equilibra.backend.login.dto;

public record LoginResponse(
        String token,
        String nome,
        String email,
        String perfil
) {
}
