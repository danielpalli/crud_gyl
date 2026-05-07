package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleVentaRequestDto(
    @NotNull(message = "El idProducto no puede ser nulo")
    @Positive(message = "El idProducto debe ser un número positivo")
    Long idProducto,

    @NotNull(message = "La cantidad no puede ser nula")
    @Min(value = 1, message = "La cantidad debe ser como mínimo 1")
    Integer cantidad
) {}
