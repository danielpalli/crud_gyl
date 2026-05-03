package com.gyl.CrudGyL.dto.response;

import java.time.LocalDate;
import java.util.List;

public record VentaResponseDto(
    Long idVenta,
    LocalDate fechaVenta,
    Double total,
    Long idCliente,
    String nombreCliente,
    List<DetalleVentaResponseDto> detalles
) {
}
