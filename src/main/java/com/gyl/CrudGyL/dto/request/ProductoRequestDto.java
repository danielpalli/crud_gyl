package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductoRequestDto(
    @NotBlank(message = "El nombre no puede ser vacio")
    String nombre,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a Cero")
    Double precio,

    @NotNull(message = "El stock es obligatorio")
    @Min(value=0, message = "El stock no puede ser negativo")
    Integer stock,

    @NotNull(message = "El id del tipo de producto es obligatorio")
    Long idTipoProducto
) {}