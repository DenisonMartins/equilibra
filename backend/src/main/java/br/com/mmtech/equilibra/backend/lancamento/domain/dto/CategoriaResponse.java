package br.com.mmtech.equilibra.backend.lancamento.domain.dto;

import br.com.mmtech.equilibra.backend.lancamento.domain.model.Categoria;

public record CategoriaResponse(
        Long id,
        String nome,
        String cor,
        String icone
) {
    public static CategoriaResponse fromEntity(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome(),
                categoria.getCor(),
                categoria.getIcone()
        );
    }
}
