package com.gyl.CrudGyL.dto.response;

public record ClienteResponseDto(
    Long id_cliente,
    String nombre,
    String apellido,
    String correo,
    String telefono,
    String dirreccion
) {
}
