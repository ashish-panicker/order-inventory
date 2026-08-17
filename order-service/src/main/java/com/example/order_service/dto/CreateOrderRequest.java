package com.example.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * CreateOrderRequest record.
 * Data Transfer Object representing a CreateOrderRequest payload.
 */
public record CreateOrderRequest(
        @NotBlank String customerId,
        @NotEmpty @Valid List<OrderItemRequest> items
) {}
