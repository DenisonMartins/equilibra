package br.com.mmtech.equilibra.backend.convite.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConviteFilter(
        String email,
        @JsonProperty("expira_em_inicial")
        LocalDate expiraEmInicial,
        @JsonProperty("expira_em_final")
        LocalDate expiraEmFinal,
        @JsonProperty("criado_em_inicial")
        LocalDate criadoEmInicial,
        @JsonProperty("criado_em_final")
        LocalDate criadoEmFinal,
        Boolean utilizado
) {
}
