package com.gyl.CrudGyL.dto.response;

public record ClienteResponseDto(
    Long idCliente,
    String nombre,
    String apellido,
    String correo,
    String telefono,
    String direccion
) {
}