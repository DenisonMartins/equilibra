package br.com.mmtech.equilibra.backend.lancamento.controller;

import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoFiltro;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoRequest;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoResponse;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.ResumoFinanceiro;
import br.com.mmtech.equilibra.backend.lancamento.service.LancamentoService;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("lancamentos")
@RequiredArgsConstructor
@Tag(name = "Lançamentos", description = "Gestão de receitas e despesas com filtragem e sumários financeiros")
@Slf4j
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @Operation(summary = "Listar lançamentos paginados", description = "Retorna lista paginada de lançamentos de acordo com os filtros aplicados")
    @GetMapping
    public Page<LancamentoResponse> listar(@AuthenticationPrincipal Usuario usuario,
                                           LancamentoFiltro filtro,
                                           @PageableDefault(size = 15, sort = "data", direction = Sort.Direction.DESC)
                                           Pageable pageable) {
        log.info("Consulta paginada de lançamentos solicitada");
        return lancamentoService.listar(usuario, filtro, pageable);
    }

    @Operation(summary = "Sumário mensal", description = "Calcula totais de receita, despesa e saldo do mês especificado")
    @GetMapping("resumo")
    public ResumoFinanceiro obterResumo(@AuthenticationPrincipal Usuario usuario,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth competencia) {
        log.info("Cálculo de resumo financeiro solicitado: competencia={}", competencia);
        return lancamentoService.obterResumo(usuario, competencia);
    }

    @Operation(summary = "Criar novo lançamento")
    @ApiResponse(responseCode = "201", description = "Lançamento registado com sucesso")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LancamentoResponse cadastrar(@AuthenticationPrincipal Usuario usuario,
                                         @RequestBody @Valid LancamentoRequest request) {
        log.info("Registo de novo lançamento: tipo={} | valor={}", request.tipo(), request.valor());
        return lancamentoService.criar(usuario, request);
    }

    @Operation(summary = "Atualizar lançamento existente")
    @PutMapping("{id}")
    public LancamentoResponse atualizar(@PathVariable Long id,
                                        @AuthenticationPrincipal Usuario usuario,
                                        @RequestBody @Valid LancamentoRequest request) {
        log.info("Atualização de lançamento solicitada: id={}", id);
        return lancamentoService.atualizar(id, usuario, request);
    }

    @Operation(summary = "Remover lançamento")
    @ApiResponse(responseCode = "204", description = "Lançamento removido com sucesso")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuario) {
        log.info("Remoção de lançamento solicitada: id={}", id);
        lancamentoService.excluir(id, usuario);
    }
}
