package br.com.mmtech.equilibra.backend.convite.controller;

import br.com.mmtech.equilibra.backend.convite.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteRequest;
import br.com.mmtech.equilibra.backend.convite.entity.Convite_;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
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
public class ConviteController {

    private final ConviteService conviteService;

    @PostMapping
    public ConviteResponse criar(@RequestBody @Valid ConviteRequest conviteRequest) {
        return conviteService.criar(conviteRequest.email());
    }

    @PostMapping("listagem")
    public Page<ConviteResponse> listar(@RequestBody ConviteFilter conviteFilter,
                                        @PageableDefault(size = 15, sort = Convite_.CRIADO_EM, direction = Sort.Direction.DESC) Pageable pageable) {
        return conviteService.listar(conviteFilter, pageable);
    }
}
