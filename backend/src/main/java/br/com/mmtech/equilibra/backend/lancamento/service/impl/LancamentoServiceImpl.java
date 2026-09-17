package br.com.mmtech.equilibra.backend.lancamento.service.impl;

import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoFiltro;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoRequest;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoResponse;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.ResumoFinanceiro;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Categoria;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.TipoLancamento;
import br.com.mmtech.equilibra.backend.lancamento.repository.CategoriaRepository;
import br.com.mmtech.equilibra.backend.lancamento.repository.LancamentoRepository;
import br.com.mmtech.equilibra.backend.lancamento.repository.specifications.LancamentoSpecs;
import br.com.mmtech.equilibra.backend.lancamento.service.LancamentoService;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class LancamentoServiceImpl implements LancamentoService {

    private final LancamentoRepository lancamentoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<LancamentoResponse> listar(Usuario usuario, LancamentoFiltro filtro, Pageable pageable) {
        return lancamentoRepository.findAll(LancamentoSpecs.comFiltro(usuario, filtro), pageable)
                .map(LancamentoResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public ResumoFinanceiro obterResumo(Usuario usuario, YearMonth competencia) {

        LocalDate inicio = competencia.atDay(1);
        LocalDate fim = competencia.atEndOfMonth();

        BigDecimal receitas = lancamentoRepository.somarPorTipoEPeriodo(usuario, TipoLancamento.RECEITA, inicio, fim);
        BigDecimal despesas = lancamentoRepository.somarPorTipoEPeriodo(usuario, TipoLancamento.DESPESA, inicio, fim);
        BigDecimal saldo = receitas.subtract(despesas);

        return new ResumoFinanceiro(receitas, despesas, saldo);
    }

    @Transactional
    @Override
    public LancamentoResponse criar(Usuario usuario, LancamentoRequest request) {
        Categoria categoria = buscarCategoriaUsuario(request.categoriaId(), usuario);

        Lancamento lancamento = Lancamento.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .data(request.data())
                .tipo(request.tipo())
                .pago(request.pago())
                .categoria(categoria)
                .usuario(usuario)
                .build();

        return LancamentoResponse.fromEntity(lancamentoRepository.save(lancamento));
    }

    @Transactional
    @Override
    public LancamentoResponse atualizar(Long id, Usuario usuario, LancamentoRequest request) {
        Lancamento lancamento = lancamentoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));

        Categoria categoria = buscarCategoriaUsuario(request.categoriaId(), usuario);

        lancamento.setDescricao(request.descricao());
        lancamento.setValor(request.valor());
        lancamento.setData(request.data());
        lancamento.setTipo(request.tipo());
        lancamento.setPago(request.pago());
        lancamento.setCategoria(categoria);

        return LancamentoResponse.fromEntity(lancamentoRepository.save(lancamento));
    }

    @Transactional
    @Override
    public void excluir(Long id, Usuario usuario) {
        Lancamento lancamento = lancamentoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new EntityNotFoundException("Lançamento não encontrado"));

        lancamentoRepository.delete(lancamento);
    }

    private Categoria buscarCategoriaUsuario(Long categoriaId, Usuario usuario) {
        return categoriaRepository.findByIdAndUsuario(categoriaId, usuario)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada"));
    }
}
