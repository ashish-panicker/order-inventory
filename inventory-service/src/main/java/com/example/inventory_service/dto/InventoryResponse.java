package com.example.inventory_service.dto;

/**
 * InventoryResponse record.
 * Data Transfer Object representing a InventoryResponse payload.
 */
public record InventoryResponse(
        String productId,
        Integer quantity,
        boolean inStock,
        String lastUpdated
) {}
