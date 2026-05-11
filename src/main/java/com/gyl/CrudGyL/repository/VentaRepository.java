package com.gyl.CrudGyL.repository;

import com.gyl.CrudGyL.dto.response.ResumenPerfilClienteResponseDto;
import com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto;
import com.gyl.CrudGyL.dto.response.TopProductoResponseDto;
import com.gyl.CrudGyL.entity.Venta;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>, JpaSpecificationExecutor<Venta> {

    @EntityGraph(attributePaths = {"cliente", "detalles", "detalles.producto"})
    Optional<Venta> findByIdVenta(Long idVenta);

    @EntityGraph(attributePaths = {"cliente", "detalles", "detalles.producto"})
    List<Venta> findDistinctByIdVentaIn(Collection<Long> ids);

    @Query("SELECT new com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto(" +
           "COALESCE(SUM(CASE WHEN v.fechaAnulacion IS NULL THEN v.total ELSE 0.0 END), 0.0), " +
           "COALESCE(SUM(CASE WHEN v.fechaAnulacion IS NOT NULL THEN v.total ELSE 0.0 END), 0.0)) " +
           "FROM Venta v")
    ResumenVentasResponseDto obtenerResumenGlobal();

    @Query("SELECT new com.gyl.CrudGyL.dto.response.ResumenVentasResponseDto(" +
           "COALESCE(SUM(CASE WHEN v.fechaAnulacion IS NULL THEN v.total ELSE 0.0 END), 0.0), " +
           "COALESCE(SUM(CASE WHEN v.fechaAnulacion IS NOT NULL THEN v.total ELSE 0.0 END), 0.0)) " +
           "FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin")
    ResumenVentasResponseDto obtenerResumenPorRango(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT new com.gyl.CrudGyL.dto.response.ResumenPerfilClienteResponseDto(" +
           "COALESCE(SUM(CASE WHEN v.fechaAnulacion IS NULL THEN v.total ELSE 0.0 END), 0.0), " +
           "COALESCE(SUM(CASE WHEN v.fechaAnulacion IS NOT NULL THEN v.total ELSE 0.0 END), 0.0), " +
           "COUNT(v), " +
           "MAX(v.fechaVenta)) " +
           "FROM Venta v WHERE v.cliente.idCliente = :idCliente")
    ResumenPerfilClienteResponseDto obtenerResumenCliente(@Param("idCliente") Long idCliente);

    @Query("""
            SELECT new com.gyl.CrudGyL.dto.response.TopProductoResponseDto(
                p.idProducto,
                p.nombreProducto,
                SUM(d.cantidad),
                COALESCE(SUM(d.subtotal), 0.0)
            )
            FROM DetalleVenta d
            JOIN d.producto p
            JOIN d.venta v
            WHERE v.fechaAnulacion IS NULL
            GROUP BY p.idProducto, p.nombreProducto
            ORDER BY SUM(d.cantidad) DESC, COALESCE(SUM(d.subtotal), 0.0) DESC, p.nombreProducto ASC
            """)
    List<TopProductoResponseDto> obtenerTopProductosMasVendidos(Pageable limite);

    @Modifying
    @Query(value = "UPDATE ventas SET fecha_anulacion = NULL WHERE id_venta = :id", nativeQuery = true)
    void reactivarVenta(@Param("id") Long id);
}
