package com.gyl.CrudGyL.specification;

import com.gyl.CrudGyL.entity.Producto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductoSpecification {

    public static Specification<Producto> conFiltros(String estado, String busqueda) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            if ("activos".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNull(root.get("fechaBaja")));
                predicados.add(cb.isTrue(root.get("estadoProducto")));
            } else if ("inactivos".equalsIgnoreCase(estado)) {
                predicados.add(cb.or(
                        cb.isNotNull(root.get("fechaBaja")),
                        cb.isFalse(root.get("estadoProducto"))));
            }

            if (busqueda != null && !busqueda.isBlank()) {
                String patron = "%" + busqueda.toLowerCase() + "%";
                predicados.add(cb.like(cb.lower(root.get("nombreProducto")), patron));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
