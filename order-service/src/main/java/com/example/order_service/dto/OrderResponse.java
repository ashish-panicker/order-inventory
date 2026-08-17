package com.example.order_service.dto;

import java.math.BigDecimal;

public record OrderResponse(
        String orderId,
        String status,
        BigDecimal totalAmount,
        String createdAt
) {}
