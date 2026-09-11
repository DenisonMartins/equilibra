package br.com.mmtech.equilibra.backend.convite.dto;

public record ValidarConviteResponse(
        boolean valido,
        String email
) {
}
