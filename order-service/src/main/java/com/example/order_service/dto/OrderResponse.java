package com.example.order_service.dto;

import java.math.BigDecimal;

/**
 * OrderResponse record.
 * Data Transfer Object representing a OrderResponse payload.
 */
public record OrderResponse(
        String orderId,
        String status,
        BigDecimal totalAmount,
        String createdAt
) {}
