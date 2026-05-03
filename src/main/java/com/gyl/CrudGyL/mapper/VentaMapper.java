package com.gyl.CrudGyL.mapper;

import com.gyl.CrudGyL.dto.request.VentaRequestDto;
import com.gyl.CrudGyL.dto.response.DetalleVentaResponseDto;
import com.gyl.CrudGyL.dto.response.VentaResponseDto;
import com.gyl.CrudGyL.entity.DetalleVenta;
import com.gyl.CrudGyL.entity.Venta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaMapper {

    private VentaMapper() {
    }

    public static Venta toEntity(VentaRequestDto dto) {
        Venta venta = new Venta();
        venta.setFechaVenta(LocalDate.now());
        venta.setTotal(0.0);
        return venta;
    }

    public static VentaResponseDto toResponseDto(Venta venta) {
        return new VentaResponseDto(
            venta.getIdVenta(),
            venta.getFechaVenta(),
            venta.getTotal(),
            venta.getCliente().getIdCliente(),
            venta.getCliente().getNombre() + " " + venta.getCliente().getApellido(),
            new ArrayList<>()
        );
    }

    public static VentaResponseDto toResponseDto(Venta venta, List<DetalleVenta> detalles) {
        List<DetalleVentaResponseDto> detallesDto = detalles.stream()
            .map(VentaMapper::toDetalleResponseDto)
            .toList();

        return new VentaResponseDto(
            venta.getIdVenta(),
            venta.getFechaVenta(),
            venta.getTotal(),
            venta.getCliente().getIdCliente(),
            venta.getCliente().getNombre() + " " + venta.getCliente().getApellido(),
            detallesDto
        );
    }

    public static void updateEntity(Venta venta, VentaRequestDto dto) {
        venta.setFechaVenta(LocalDate.now());
    }

    public static DetalleVentaResponseDto toDetalleResponseDto(DetalleVenta detalle) {
        return new DetalleVentaResponseDto(
            detalle.getIdDetalleVenta(),
            detalle.getProducto().getIdProducto(),
            detalle.getProducto().getNombre(),
            detalle.getCantidad(),
            detalle.getPrecioUnitario(),
            detalle.getSubtotal()
        );
    }
}
