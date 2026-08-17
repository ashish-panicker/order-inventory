package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * OrderItemRequest record.
 * Data Transfer Object representing a OrderItemRequest payload.
 */
public record OrderItemRequest(
        @NotBlank String productId,
        @NotNull @Min(1) Integer quantity,
        @NotNull BigDecimal price
) {}
