package com.gyl.CrudGyL.specification;

import com.gyl.CrudGyL.entity.Venta;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
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

    public static Specification<Venta> porCliente(Long idCliente) {
        return (root, query, cb) ->
                cb.equal(root.get("cliente").get("idCliente"), idCliente);
    }

    public static Specification<Venta> porRangoFechas(LocalDate inicio, LocalDate fin) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            LocalDate fechaInicio = (inicio != null) ? inicio : LocalDate.now();
            LocalDate fechaFin = (fin != null) ? fin : LocalDate.now();

            predicados.add(cb.greaterThanOrEqualTo(root.get("fechaVenta"), fechaInicio));
            predicados.add(cb.lessThanOrEqualTo(root.get("fechaVenta"), fechaFin));

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }
}
