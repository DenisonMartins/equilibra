package br.com.mmtech.equilibra.backend.lancamento.domain.dto;

import br.com.mmtech.equilibra.backend.lancamento.domain.model.TipoLancamento;

import java.time.LocalDate;

public record LancamentoFiltro(
        String descricao,
        TipoLancamento tipo,
        Long categoriaId,
        LocalDate dataInicio,
        LocalDate dataFim,
        Boolean pago
) {
}
