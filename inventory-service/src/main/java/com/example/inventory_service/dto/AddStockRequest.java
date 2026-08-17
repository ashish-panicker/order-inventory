package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * AddStockRequest record.
 * Data Transfer Object representing a AddStockRequest payload.
 */
public record AddStockRequest(
        @NotBlank(message = "Product ID cannot be left blank")
        @Size(max = 255)
        String productId,

        @NotNull @Min(1) Integer quantityToAdd
) {}
