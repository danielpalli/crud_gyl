package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record VentaHistorialClienteResponseDto(
    PageResponseDto<VentaResponseDto> ventas,
    ResumenPerfilClienteResponseDto resumen
) {}
