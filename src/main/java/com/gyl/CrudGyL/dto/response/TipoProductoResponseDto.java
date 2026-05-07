package com.gyl.CrudGyL.dto.response;

public record TipoProductoResponseDto(
    Long idTipoProducto,
    String nombreTipoProducto,
    String descripcion
) {
}
