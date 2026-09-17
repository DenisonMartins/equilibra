package br.com.mmtech.equilibra.backend.lancamento.controller;

import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoFiltro;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoRequest;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoResponse;
import br.com.mmtech.equilibra.backend.lancamento.domain.dto.ResumoFinanceiro;
import br.com.mmtech.equilibra.backend.lancamento.service.LancamentoService;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class LancamentoController {

    private final LancamentoService lancamentoService;

    @GetMapping
    public Page<LancamentoResponse> listar(@AuthenticationPrincipal Usuario usuario,
                                           LancamentoFiltro filtro,
                                           @PageableDefault(size = 15, sort = "data", direction = Sort.Direction.DESC)
                                           Pageable pageable) {
        return lancamentoService.listar(usuario, filtro, pageable);
    }

    @GetMapping("resumo")
    public ResumoFinanceiro obterResumo(@AuthenticationPrincipal Usuario usuario,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth competencia) {
        return lancamentoService.obterResumo(usuario, competencia);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LancamentoResponse cadastrar(@AuthenticationPrincipal Usuario usuario,
                                         @RequestBody @Valid LancamentoRequest request) {
        return lancamentoService.criar(usuario, request);
    }

    @PutMapping("{id}")
    public LancamentoResponse atualizar(@PathVariable Long id,
                                        @AuthenticationPrincipal Usuario usuario,
                                        @RequestBody @Valid LancamentoRequest request) {
        return lancamentoService.atualizar(id, usuario, request);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuario) {
        lancamentoService.excluir(id, usuario);
    }
}
