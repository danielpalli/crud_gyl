package com.gyl.CrudGyL.dto.response;

public record ProductoResponseDto(
    Long idProducto,
    Long idTipoProducto,
    String nombre,
    Double precio,
    Integer stock,
    String nombreTipoProducto
) {
}
