package br.com.mmtech.equilibra.backend.usuario.repository;

import br.com.mmtech.equilibra.backend.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
}
