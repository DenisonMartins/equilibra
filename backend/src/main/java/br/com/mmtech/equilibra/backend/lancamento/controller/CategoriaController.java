package br.com.mmtech.equilibra.backend.lancamento.controller;

import br.com.mmtech.equilibra.backend.lancamento.domain.dto.*;
import br.com.mmtech.equilibra.backend.lancamento.repository.CategoriaRepository;
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
import java.util.List;

@RestController
@RequestMapping("categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    @GetMapping
    public List<CategoriaResponse> listar(@AuthenticationPrincipal Usuario usuario) {
        return categoriaRepository.findAllByUsuario(usuario).stream().map(CategoriaResponse::fromEntity).toList();
    }
}
