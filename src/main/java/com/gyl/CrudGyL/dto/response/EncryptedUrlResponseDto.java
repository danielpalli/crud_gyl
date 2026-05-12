package com.gyl.CrudGyL.dto.response;

import lombok.Builder;

@Builder
public record EncryptedUrlResponseDto(
        Long id,
        String algoritmo,
        String devUrl,
        String prodUrl,
        String encryptedSegment,
        String decryptedStaticPath
) {}
