package br.com.mmtech.equilibra.backend.autenticacao.dto;

import br.com.mmtech.equilibra.backend.usuario.entity.Perfil;

public record LoginResponse(
        String token,
        String nome,
        String email,
        Perfil perfil
) {
}
