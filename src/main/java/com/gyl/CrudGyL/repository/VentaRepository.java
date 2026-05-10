package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {

    @Query("SELECT COALESCE(SUM(v.total), 0.0) FROM Venta v WHERE v.fechaAnulacion IS NULL")
    Double calcularTotalGanancias();

    @Query("SELECT COALESCE(SUM(v.total), 0.0) FROM Venta v WHERE v.fechaAnulacion IS NOT NULL")
    Double calcularTotalDevoluciones();

    @Modifying
    @Query(value = "UPDATE ventas SET fecha_anulacion = NULL WHERE id_venta = :id", nativeQuery = true)
    void reactivarVenta(@Param("id") Long id);
}
