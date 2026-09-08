package br.com.mmtech.equilibra.backend.convite.service;

import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarTokenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConviteService {

    ConviteResponse criar(String email);
    Page<ConviteResponse> listar(ConviteFilter conviteFilter, Pageable pageable);
    ValidarTokenResponse validarToken(String token);
    void aceitarConvite(AceiteConviteRequest aceiteConviteRequest);
}
