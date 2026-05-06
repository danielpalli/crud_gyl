package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.*;

public record ProductoRequestDto(
    @NotBlank(message = "El nombre del producto del producto no puede estar vacío")
    @Size(max = 100, message = "El nombre del producto no puede tener más de 100 caracteres")
    String nombreProducto,

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    Double precio,

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    Integer stock,

    @NotNull(message = "El idTipoProducto es obligatorio")
    @Positive(message = "El idTipoProducto debe ser un número positivo")
    Long idTipoProducto
) {}