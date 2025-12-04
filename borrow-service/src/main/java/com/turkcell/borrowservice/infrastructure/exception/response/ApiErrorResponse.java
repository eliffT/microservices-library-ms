package com.turkcell.borrowservice.infrastructure.exception.response;

import java.time.OffsetDateTime;

public record ApiErrorResponse(
        String message,
        String errorCode,           // Hatanın türünü belirten kod
        OffsetDateTime timestamp
) {
    public ApiErrorResponse(String message) {
        this(message, null, OffsetDateTime.now());
    }
}