package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DeductStockRequest(
        @NotNull @Min(1) Integer quantity
) {}
