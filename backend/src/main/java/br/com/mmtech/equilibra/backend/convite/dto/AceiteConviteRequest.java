package br.com.mmtech.equilibra.backend.convite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AceiteConviteRequest(
        @NotBlank(message = "O hash é obrigatório")
        String hash,

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
        String senha
) {
}
