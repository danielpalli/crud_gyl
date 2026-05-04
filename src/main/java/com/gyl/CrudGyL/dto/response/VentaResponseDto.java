package com.gyl.CrudGyL.dto.response;

import java.time.LocalDate;
import java.util.List;

public record VentaResponseDto(
    Long idVenta,
    Long idCliente,
    LocalDate fechaVenta,
    Double total,
    String nombreCliente,
    List<DetalleVentaResponseDto> detalles
) {
}
