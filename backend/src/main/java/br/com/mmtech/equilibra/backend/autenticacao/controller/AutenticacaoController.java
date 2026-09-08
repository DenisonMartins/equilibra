package br.com.mmtech.equilibra.backend.autenticacao.controller;

import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarTokenResponse;
import br.com.mmtech.equilibra.backend.convite.service.ConviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autneticacao")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final ConviteService conviteService;

    @GetMapping("validar-token")
    public ValidarTokenResponse validarToken(@RequestParam String token) {
        return conviteService.validarToken(token);
    }

    @PostMapping("aceitar-convite")
    public void aceitarConvite(@RequestBody @Valid AceiteConviteRequest dto) {
        conviteService.aceitarConvite(dto);
    }
}
