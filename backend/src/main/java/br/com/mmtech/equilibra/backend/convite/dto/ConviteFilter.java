package br.com.mmtech.equilibra.backend.convite.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ConviteFilter(
        String email,
        @JsonProperty("expira_em_inicial")
        LocalDateTime expiraEmInicial,
        @JsonProperty("expira_em_final")
        LocalDateTime expiraEmFinal,
        @JsonProperty("criado_em_inicial")
        LocalDateTime criadoEmInicial,
        @JsonProperty("criado_em_final")
        LocalDateTime criadoEmFinal,
        Boolean utilizado
) {
}
