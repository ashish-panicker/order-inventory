package com.example.order_service.common.dto;

/**
 * ApiErrorResponse record.
 * Data Transfer Object representing a ApiErrorResponse payload.
 */
public record ApiErrorResponse(
    boolean success,
    String timestamp,
    int status,
    String error,
    String message,
    String path
) {
    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(
            false,
            java.time.OffsetDateTime.now().toString(),
            status,
            error,
            message,
            path
        );
    }
}
