package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddStockRequest(
        @NotBlank @Size(max = 255) String productId,
        @NotNull @Min(1) Integer quantityToAdd
) {}
