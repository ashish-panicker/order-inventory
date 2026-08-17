package com.example.inventory_service.service.impl;

import com.example.inventory_service.common.exception.ResourceNotFoundException;
import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.dto.DeductStockRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.entity.InventoryItem;
import com.example.inventory_service.mapper.InventoryMapper;
import com.example.inventory_service.repository.InventoryRepository;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
/**
 * InventoryServiceImpl class.
 * Implementation of the business logic for Inventory management.
 */
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public InventoryResponse getStockInfo(String productId) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID does not exist in inventory"));
        return inventoryMapper.toResponse(item);
    }

    @Override
    public Page<InventoryResponse> listInventory(Pageable pageable) {
        return inventoryRepository.findAll(pageable)
                .map(inventoryMapper::toResponse);
    }

    @Override
    @Transactional
    public InventoryResponse addStock(AddStockRequest request) {
        InventoryItem item = inventoryRepository.findByProductId(request.productId())
                .orElseGet(() -> {
                    InventoryItem newItem = new InventoryItem();
                    newItem.setProductId(request.productId());
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + request.quantityToAdd());
        item.setInStock(item.getQuantity() > 0);
        
        InventoryItem saved = inventoryRepository.save(item);
        return inventoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InventoryResponse deductStock(String productId, DeductStockRequest request) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID does not exist in inventory"));

        if (item.getQuantity() < request.quantity()) {
            throw new IllegalArgumentException("Insufficient stock for product: " + productId);
        }

        item.setQuantity(item.getQuantity() - request.quantity());
        item.setInStock(item.getQuantity() > 0);

        InventoryItem saved = inventoryRepository.save(item);
        return inventoryMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void deleteInventory(String productId) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product ID does not exist in inventory"));
        inventoryRepository.delete(item);
    }
}
