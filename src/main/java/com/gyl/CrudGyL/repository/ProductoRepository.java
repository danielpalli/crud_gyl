package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
    boolean existsByNombreAndIdProductoNot(String nombre, Long id);
}
