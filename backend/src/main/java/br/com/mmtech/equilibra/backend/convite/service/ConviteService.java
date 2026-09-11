package br.com.mmtech.equilibra.backend.convite.service;

import br.com.mmtech.equilibra.backend.convite.dto.AceiteConviteRequest;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.dto.ConviteResponse;
import br.com.mmtech.equilibra.backend.convite.dto.ValidarConviteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConviteService {

    ConviteResponse criar(String email);
    Page<ConviteResponse> listar(ConviteFilter conviteFilter, Pageable pageable);
    ValidarConviteResponse validarHash(String hash);
    void aceitarConvite(AceiteConviteRequest aceiteConviteRequest);
}
