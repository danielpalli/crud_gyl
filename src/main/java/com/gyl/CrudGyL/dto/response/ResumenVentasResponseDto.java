package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record ResumenVentasResponseDto(
    Double totalGanancias,
    Double totalDevoluciones
) {}
