package br.com.mmtech.equilibra.backend.convite.controller;

import br.com.mmtech.equilibra.backend.convite.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteRequest;
import br.com.mmtech.equilibra.backend.convite.entity.Convite_;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/convites")
@RequiredArgsConstructor
@Tag(name = "Administração - Convites", description = "Gestão de emissão e acompanhamento de convites (Exclusivo Administradores)")
public class ConviteController {

    private final ConviteService conviteService;

    @Operation(summary = "Emitir novo convite",
            description = "Gera um token de acesso de 48h e despacha o e-mail via SMTP.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Convite criado e disparado"),
            @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou inválido"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer ROLE_ADMIN)")
    })
    @PostMapping
    public ConviteResponse criar(@RequestBody @Valid ConviteRequest conviteRequest) {
        return conviteService.criar(conviteRequest.email());
    }

    @Operation(summary = "Listar convites paginados com Specification",
            description = "Consulta convites com filtros opcionais por e-mail, status e intervalo de datas.")
    @PostMapping("listagem")
    public Page<ConviteResponse> listar(@RequestBody ConviteFilter conviteFilter,
                                        @PageableDefault(size = 15, sort = Convite_.CRIADO_EM, direction = Sort.Direction.DESC) Pageable pageable) {
        return conviteService.listar(conviteFilter, pageable);
    }
}
