package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long> {
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdTipoProductoNot(String nombre, Long id);
}
