package com.gyl.CrudGyL.specification;

import com.gyl.CrudGyL.entity.Venta;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VentaSpecification {

    public static Specification<Venta> conFiltros(String estado) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            if ("activas".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNull(root.get("fechaAnulacion")));
            } else if ("anuladas".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNotNull(root.get("fechaAnulacion")));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
