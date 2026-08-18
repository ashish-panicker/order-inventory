package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DeductStockRequest record.
 * Data Transfer Object representing a DeductStockRequest payload.
 */
@Schema(description = "Request object for deducting stock")
public record DeductStockRequest(
        @NotNull @Min(1) Integer quantity
) {}
