package br.com.mmtech.equilibra.backend.convite.dto;

public record ValidarTokenResponse(
        boolean valido,
        String email
) {
}
