package com.example.order_service.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * OrderResponse record.
 * Data Transfer Object representing a OrderResponse payload.
 */
@Schema(description = "Response object containing order details")
public record OrderResponse(
        String orderId,
        String status,
        BigDecimal totalAmount,
        String createdAt
) {}
