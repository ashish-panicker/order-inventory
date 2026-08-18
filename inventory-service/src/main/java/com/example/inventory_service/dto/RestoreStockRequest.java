package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for restoring stock")
public record RestoreStockRequest(
        @NotNull @Min(1) Integer quantity
) {}
