package br.com.mmtech.equilibra.backend.lancamento.repository;

import br.com.mmtech.equilibra.backend.lancamento.domain.model.Categoria;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findAllByUsuario(Usuario usuario);

    Optional<Categoria> findByIdAndUsuarioId(Long id, Long usuarioId);
}
