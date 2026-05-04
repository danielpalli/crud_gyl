package com.gyl.CrudGyL.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ErrorResponseDto(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    Map<String, String> validationErrors
) {}
