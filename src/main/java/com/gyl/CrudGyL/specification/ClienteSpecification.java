package com.gyl.CrudGyL.specification;

import com.gyl.CrudGyL.entity.Cliente;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClienteSpecification {

    public static Specification<Cliente> conFiltros(String estado, String busqueda) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            if ("activos".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNull(root.get("fechaBaja")));
            } else if ("inactivos".equalsIgnoreCase(estado)) {
                predicados.add(cb.isNotNull(root.get("fechaBaja")));
            }

            if (busqueda != null && !busqueda.isBlank()) {
                String patron = "%" + busqueda.toLowerCase() + "%";
                Predicate nombre = cb.like(cb.lower(root.get("nombre")), patron);
                Predicate apellido = cb.like(cb.lower(root.get("apellido")), patron);
                Predicate dni = cb.like(cb.lower(root.get("dni")), patron);

                predicados.add(cb.or(nombre, apellido, dni));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
