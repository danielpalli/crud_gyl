package com.gyl.CrudGyL.dto.response;

import lombok.Builder;
import java.time.Instant;

@Builder
public record ProductoResponseDto(
    Long idProducto,
    Long idTipoProducto,
    String nombreProducto,
    Double precio,
    Integer stock,
    String nombreTipoProducto,
    Instant fechaBaja
) {}
