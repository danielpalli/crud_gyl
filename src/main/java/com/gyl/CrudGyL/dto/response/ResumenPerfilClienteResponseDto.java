package com.gyl.CrudGyL.dto.response;

import lombok.Builder;
import java.time.LocalDate;

@Builder
public record ResumenPerfilClienteResponseDto(
    Double totalInvertido,
    Double totalReembolsado,
    Long cantidadPedidos,
    LocalDate fechaUltimaCompra
) {}
