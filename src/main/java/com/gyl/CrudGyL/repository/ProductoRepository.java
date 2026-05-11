package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    List<Producto> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    boolean existsByNombreProducto(String nombreProducto);

    boolean existsByNombreProductoAndIdProductoNot(String nombreProducto, Long id);

    boolean existsByTipoProductoIdTipoProducto(Long idTipoProducto);

    @Query("SELECT COUNT(d) > 0 FROM DetalleVenta d WHERE d.producto.idProducto = :id")
    boolean existsInVentas(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE productos SET fecha_baja = NULL, estado_producto = true WHERE id_producto = :id", nativeQuery = true)
    void restaurarProducto(@Param("id") Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE Producto p
            SET p.estadoProducto = false
            WHERE p.tipoProducto.idTipoProducto = :idTipoProducto
            """)
    int inactivarPorTipoProducto(@Param("idTipoProducto") Long idTipoProducto);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
            UPDATE Producto p
            SET p.estadoProducto = true
            WHERE p.tipoProducto.idTipoProducto = :idTipoProducto
              AND p.fechaBaja IS NULL
            """)
    int restaurarPorTipoProducto(@Param("idTipoProducto") Long idTipoProducto);
}
