package com.gyl.CrudGyL.dto.request.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoProductoUpdateRequestDto(
    @Size(max = 50, message = "El nombre del tipo de producto no puede tener más de 50 caracteres")
    String nombreTipoProducto,

    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    String descripcion
) {
}
