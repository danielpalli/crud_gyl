package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByFechaBajaIsNull();

    List<Producto> findByFechaBajaIsNotNull();

    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    boolean existsByNombreProducto(String nombreProducto);

    boolean existsByNombreProductoAndIdProductoNot(String nombreProducto, Long id);

    boolean existsByTipoProductoIdTipoProducto(Long idTipoProducto);

    @Query("SELECT COUNT(d) > 0 FROM DetalleVenta d WHERE d.producto.idProducto = :id")
    boolean existsInVentas(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE productos SET fecha_baja = NULL WHERE id_producto = :id", nativeQuery = true)
    void restaurarProducto(@Param("id") Long id);
}
