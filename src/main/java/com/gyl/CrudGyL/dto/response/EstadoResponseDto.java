package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record EstadoResponseDto(
    Long id,
    String nombre,
    String mensaje,
    String estado
) {}
