package br.com.mmtech.equilibra.backend.convite.domain.dto;

import br.com.mmtech.equilibra.backend.convite.domain.model.Convite;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ConviteResponse(
        Long id,
        String email,
        String token,
        @JsonProperty("expira_em")
        LocalDateTime expiraEm,
        boolean utilizado,
        @JsonProperty("criado_em")
        LocalDateTime criadoEm
) {
    public static ConviteResponse of(Convite convite) {
        return new ConviteResponse(
                convite.getId(),
                convite.getEmail(),
                convite.getHash(),
                convite.getExpiraEm(),
                convite.isUtilizado(),
                convite.getCriadoEm()
        );
    }
}
