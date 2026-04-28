package com.gyl.CrudGyL.dto.response;

public record ProductoResponseDto(
    Long id,
    String nombre,
    Double precio,
    Integer stock) {
}
