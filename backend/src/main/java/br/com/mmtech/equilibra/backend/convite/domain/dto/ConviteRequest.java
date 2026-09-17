package br.com.mmtech.equilibra.backend.convite.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ConviteRequest(
        @NotBlank(message = "O email é obrigatório")
        @Email
        String email
) {
}
