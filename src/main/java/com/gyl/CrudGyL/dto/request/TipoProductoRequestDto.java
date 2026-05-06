package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoProductoRequestDto(
    @NotBlank(message = "El nombre del tipo de producto no puede estar vacío")
    @Size(max = 50, message = "El nombre del tipo de producto no puede tener más de 50 caracteres")
    String nombreTipoProducto,

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    String descripcion
) {
}
