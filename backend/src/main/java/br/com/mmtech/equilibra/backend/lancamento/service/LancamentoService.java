package br.com.mmtech.equilibra.backend.lancamento.service;

import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoFiltro;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoRequest;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoResponse;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.ResumoFinanceiro;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;

public interface LancamentoService {

    Page<LancamentoResponse> listar(Usuario usuario, LancamentoFiltro filtro, Pageable pageable);

    ResumoFinanceiro obterResumo(Usuario usuario, YearMonth competencia);

    LancamentoResponse criar(Usuario usuario, LancamentoRequest request);

    LancamentoResponse atualizar(Long id, Usuario usuario, LancamentoRequest request);

    void excluir(Long id, Usuario usuario);
}
