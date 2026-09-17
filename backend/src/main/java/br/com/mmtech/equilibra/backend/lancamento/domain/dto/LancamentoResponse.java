package br.com.mmtech.equilibra.backend.lancamento.domain.dto;

import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        TipoLancamento tipo,
        boolean pago,
        CategoriaResponse categoria
) {
    public static LancamentoResponse fromEntity(Lancamento lancamento) {
        return new LancamentoResponse(
                lancamento.getId(),
                lancamento.getDescricao(),
                lancamento.getValor(),
                lancamento.getData(),
                lancamento.getTipo(),
                lancamento.isPago(),
                CategoriaResponse.fromEntity(lancamento.getCategoria())
        );
    }
}
