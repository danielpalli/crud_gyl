package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.TipoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoProductoRepository extends JpaRepository<TipoProducto, Long>, JpaSpecificationExecutor<TipoProducto> {

    boolean existsByNombreTipoProducto(String nombreTipoProducto);

    boolean existsByNombreTipoProductoAndIdTipoProductoNot(String nombreTipoProducto, Long id);

    @Modifying
    @Query(value = "UPDATE tipo_producto SET fecha_baja = NULL WHERE id_tipo_producto = :id", nativeQuery = true)
    void restaurarTipoProducto(@Param("id") Long id);
}
