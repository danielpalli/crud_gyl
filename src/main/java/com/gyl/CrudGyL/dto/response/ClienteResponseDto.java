package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record ClienteResponseDto(
    Long idCliente,
    String nombre,
    String apellido,
    String correo,
    String telefono,
    String direccion
) {}