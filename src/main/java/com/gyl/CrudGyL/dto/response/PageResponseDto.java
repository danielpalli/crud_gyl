package com.gyl.CrudGyL.dto.response;

import lombok.Builder;
import java.util.List;

@Builder
public record PageResponseDto<T>(
    List<T> contenido,
    int numeroPagina,
    int tamanioPagina,
    long totalElementos,
    int totalPaginas,
    boolean esUltima
) {}
