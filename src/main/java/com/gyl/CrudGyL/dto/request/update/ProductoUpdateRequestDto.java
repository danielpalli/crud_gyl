package com.gyl.CrudGyL.dto.request.update;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record ProductoUpdateRequestDto(
    @Size(max = 100, message = "El nombre del producto no puede tener más de 100 caracteres")
    String nombreProducto,

    @Positive(message = "El precio debe ser mayor a cero")
    Double precio,

    @Min(value = 0, message = "El stock no puede ser negativo")
    Integer stock,

    Long idTipoProducto
) {}