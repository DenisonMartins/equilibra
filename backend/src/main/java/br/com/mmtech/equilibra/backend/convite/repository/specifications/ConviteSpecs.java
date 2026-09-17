package br.com.mmtech.equilibra.backend.convite.repository.specifications;

import br.com.mmtech.equilibra.backend.convite.domain.dto.ConviteFilter;
import br.com.mmtech.equilibra.backend.convite.domain.model.Convite;
import br.com.mmtech.equilibra.backend.convite.domain.model.Convite_;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ConviteSpecs {

    private ConviteSpecs() {
    }

    public static Specification<Convite> comFiltro(ConviteFilter conviteFilter) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(conviteFilter)) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(conviteFilter.email())) {
                predicates.add(criteriaBuilder.like(root.get(Convite_.email), "%" + conviteFilter.email() + "%"));
            }

            if (Objects.nonNull(conviteFilter.expiraEmInicial())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Convite_.expiraEm),
                        conviteFilter.expiraEmInicial().atStartOfDay())
                );
            }

            if (Objects.nonNull(conviteFilter.expiraEmFinal())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Convite_.expiraEm),
                        conviteFilter.expiraEmFinal().atTime(LocalTime.MAX))
                );
            }

            if (Objects.nonNull(conviteFilter.criadoEmInicial())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Convite_.criadoEm),
                        conviteFilter.criadoEmInicial().atStartOfDay())
                );
            }

            if (Objects.nonNull(conviteFilter.criadoEmFinal())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Convite_.criadoEm),
                        conviteFilter.criadoEmFinal().atTime(LocalTime.MAX))
                );
            }

            if (Objects.nonNull(conviteFilter.utilizado())) {
                predicates.add(criteriaBuilder.equal(root.get(Convite_.utilizado), conviteFilter.utilizado()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
