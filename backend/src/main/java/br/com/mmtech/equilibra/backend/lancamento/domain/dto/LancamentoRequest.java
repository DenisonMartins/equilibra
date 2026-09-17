package br.com.mmtech.equilibra.backend.lancamento.domain.dto;

import br.com.mmtech.equilibra.backend.lancamento.domain.model.TipoLancamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoRequest(
        @NotBlank(message = "A descrição é obrigatória")
        @Size(min = 3, max = 255)
        String descricao,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A data é obrigatória")
        LocalDate data,

        @NotNull(message = "O tipo é obrigatório")
        TipoLancamento tipo,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId,

        boolean pago
) {
}
