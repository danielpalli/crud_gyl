package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record DetalleVentaResponseDto(
    Long idDetalleVenta,
    Long idProducto,
    String nombreProducto,
    Integer cantidad,
    Double precioUnitario,
    Double subtotal
) {}
