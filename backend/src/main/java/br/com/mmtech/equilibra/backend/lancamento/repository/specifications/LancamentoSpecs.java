package br.com.mmtech.equilibra.backend.lancamento.repository.specifications;

import br.com.mmtech.equilibra.backend.lancamento.domain.dto.LancamentoFiltro;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Categoria_;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento;
import br.com.mmtech.equilibra.backend.lancamento.domain.model.Lancamento_;
import br.com.mmtech.equilibra.backend.usuario.domain.model.Usuario;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class LancamentoSpecs {

    private LancamentoSpecs() {
    }

    public static Specification<Lancamento> comFiltro(Usuario usuario, LancamentoFiltro filtro) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get(Lancamento_.usuario), usuario));

            if (isNull(filtro)) {
                return builder.and(predicates.toArray(new Predicate[0]));
            }

            if (StringUtils.isNotBlank(filtro.descricao())) {
                predicates.add(builder.like(
                        builder.lower(root.get(Lancamento_.descricao)), "%" + filtro.descricao().trim().toLowerCase() + "%"));
            }

            if (nonNull(filtro.tipo())) {
                predicates.add(builder.equal(root.get(Lancamento_.tipo), filtro.tipo()));
            }

            if (nonNull(filtro.categoriaId())) {
                predicates.add(builder.equal(root.get(Lancamento_.categoria).get(Categoria_.id), filtro.categoriaId()));
            }

            if (nonNull(filtro.dataInicio())) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.data), filtro.dataInicio()));
            }

            if (nonNull(filtro.dataFim())) {
                predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.data), filtro.dataFim()));
            }

            if (nonNull(filtro.pago())) {
                predicates.add(builder.equal(root.get(Lancamento_.pago), filtro.pago()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
