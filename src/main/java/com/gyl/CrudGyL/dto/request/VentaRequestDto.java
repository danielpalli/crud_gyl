package com.gyl.CrudGyL.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record VentaRequestDto(
    @NotNull(message = "El idCliente no puede ser nulo")
    @Positive(message = "El idCliente debe ser positivo")
    Long idCliente,

    @NotEmpty(message = "La venta debe tener al menos un detalle")
    @Valid
    List<DetalleVentaRequestDto> detalles
) {
}
