package br.com.mmtech.equilibra.backend.convite.dto;

import java.time.LocalDateTime;

public record ConviteFilter(
        String email,
        LocalDateTime expiraEmInicial,
        LocalDateTime expiraEmFinal,
        LocalDateTime criadoEmInicial,
        LocalDateTime criadoEmFinal,
        Boolean utilizado
) {
}
