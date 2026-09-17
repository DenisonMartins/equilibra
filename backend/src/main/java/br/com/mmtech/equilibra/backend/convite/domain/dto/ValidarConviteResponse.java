package br.com.mmtech.equilibra.backend.convite.domain.dto;

public record ValidarConviteResponse(
        boolean valido,
        String email
) {
}
