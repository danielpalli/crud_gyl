package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TipoProductoRequestDto(
    @NotBlank(message = "El nombre no puede estar vacío")
    String nombre,

    @NotBlank(message = "La descripción no puede estar vacía")
    String descripcion
) {
}
