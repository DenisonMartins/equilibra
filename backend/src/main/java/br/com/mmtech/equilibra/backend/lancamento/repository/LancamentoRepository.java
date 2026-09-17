package br.com.mmtech.equilibra.backend.lancamento.repository;

import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.TipoLancamento;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, JpaSpecificationExecutor<Lancamento> {
    Optional<Lancamento> findByIdAndUsuario(Long id, Usuario usuario);

    @Query("""
            select coalesce(sum(l.valor), 0)
              from Lancamento l
             where l.usuario = :usuario
               and l.tipo = :tipoLancamento
               and l.data between :inicio and :fim
            """)
    BigDecimal somarPorTipoEPeriodo(Usuario usuario, TipoLancamento tipoLancamento, LocalDate inicio, LocalDate fim);
}
