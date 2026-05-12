package com.gyl.CrudGyL.specification;

import com.gyl.CrudGyL.entity.TipoProducto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TipoProductoSpecification {

    public static Specification<TipoProducto> conFiltros(String estado, String busqueda) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            if ("activos".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNull(root.get("fechaBaja")));
            } else if ("inactivos".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNotNull(root.get("fechaBaja")));
            }

            if (busqueda != null && !busqueda.isBlank()) {
                String patron = "%" + busqueda.toLowerCase() + "%";
                predicados.add(cb.like(cb.lower(root.get("nombreTipoProducto")), patron));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
