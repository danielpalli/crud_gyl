package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record VentaHistorialResponseDto(
    PageResponseDto<VentaResponseDto> ventas,
    ResumenVentasResponseDto resumen
) {}
