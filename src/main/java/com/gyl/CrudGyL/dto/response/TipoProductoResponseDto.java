package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record TipoProductoResponseDto(
    Long idTipoProducto,
    String nombreTipoProducto,
    String descripcion
) {}
