package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record TopProductoResponseDto(
        Long idProducto,
        String nombreProducto,
        Long cantidadVendida,
        Double totalVendido
) {}
