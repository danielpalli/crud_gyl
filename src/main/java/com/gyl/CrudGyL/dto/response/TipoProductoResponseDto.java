package com.gyl.CrudGyL.dto.response;

import lombok.Builder;
import java.time.Instant;

@Builder
public record TipoProductoResponseDto(
    Long idTipoProducto,
    String nombreTipoProducto,
    String descripcion,
    Instant fechaBaja
) {}
