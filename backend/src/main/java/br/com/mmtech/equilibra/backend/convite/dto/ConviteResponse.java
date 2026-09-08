package br.com.mmtech.equilibra.backend.convite.dto;

import br.com.mmtech.equilibra.backend.convite.entity.Convite;

import java.time.LocalDateTime;

public record ConviteResponse(
        Long id,
        String email,
        String token,
        LocalDateTime expiraEm,
        boolean utilizado,
        LocalDateTime criadoEm
) {
    public static ConviteResponse of(Convite convite) {
        return new ConviteResponse(
                convite.getId(),
                convite.getEmail(),
                convite.getToken(),
                convite.getExpiraEm(),
                convite.isUtilizado(),
                convite.getCriadoEm()
        );
    }
}
