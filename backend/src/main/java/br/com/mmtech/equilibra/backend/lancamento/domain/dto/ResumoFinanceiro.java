package br.com.mmtech.equilibra.backend.lancamento.domain.dto;

import java.math.BigDecimal;

public record ResumoFinanceiro(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {
}
