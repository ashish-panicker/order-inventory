package com.example.inventory_service.dto;

public record InventoryResponse(
        String productId,
        Integer quantity,
        boolean inStock,
        String lastUpdated
) {}
