package com.gyl.CrudGyL.dto.response;

public record ProductoResponseDto(
    Long idProducto,
    Long idTipoProducto,
    String nombreProducto,
    Double precio,
    Integer stock,
    String nombreTipoProducto
) {
}
