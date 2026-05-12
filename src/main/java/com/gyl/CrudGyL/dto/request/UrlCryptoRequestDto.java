package com.gyl.CrudGyL.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UrlCryptoRequestDto(
        @NotBlank(message = "La URL no puede estar vacía")
        String url,

        String algoritmo
) {}
