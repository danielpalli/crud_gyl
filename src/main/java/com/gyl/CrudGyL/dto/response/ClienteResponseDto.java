package com.gyl.CrudGyL.dto.response;

import com.gyl.CrudGyL.enums.Genero;
import lombok.Builder;
import java.time.Instant;

@Builder
public record ClienteResponseDto(
    Long idCliente,
    String nombre,
    String apellido,
    String correo,
    String telefono,
    String direccion,
    String dni,
    Genero genero,
    String nacionalidad,
    Instant fechaBaja
) {}
