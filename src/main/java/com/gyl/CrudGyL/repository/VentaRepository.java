package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaAnulacionIsNull();

    List<Venta> findByFechaAnulacionIsNotNull();

    @Modifying
    @Query(value = "UPDATE ventas SET fecha_anulacion = NULL WHERE id_venta = :id", nativeQuery = true)
    void reactivarVenta(@Param("id") Long id);
}
