package br.com.mmtech.equilibra.backend.convite.repository;

import br.com.mmtech.equilibra.backend.convite.entity.Convite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ConviteRepository extends JpaRepository<Convite, Long>, JpaSpecificationExecutor<Convite> {
    Optional<Convite> findByHash(String token);
}
