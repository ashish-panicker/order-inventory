package com.example.order_service.common.dto;

/**
 * ApiResponse record.
 * Data Transfer Object representing a ApiResponse payload.
 */
public record ApiResponse<T>(
    boolean success,
    T data,
    String message
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }
}
