package com.example.inventory_service.service;

import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.dto.DeductStockRequest;
import com.example.inventory_service.dto.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    InventoryResponse getStockInfo(String productId);
    Page<InventoryResponse> listInventory(Pageable pageable);
    InventoryResponse addStock(AddStockRequest request);
    InventoryResponse deductStock(String productId, DeductStockRequest request);
}
