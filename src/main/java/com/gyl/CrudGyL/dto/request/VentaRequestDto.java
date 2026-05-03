package com.gyl.CrudGyL.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VentaRequestDto(
    @NotNull(message = "El id del cliente no puede ser nulo")
    Long idCliente,

    @NotEmpty(message = "La venta debe tener al menos un detalle")
    @Valid
    List<DetalleVentaRequestDto> detalles
) {
}
