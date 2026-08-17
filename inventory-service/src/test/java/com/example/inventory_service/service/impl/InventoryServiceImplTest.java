package com.example.inventory_service.service.impl;

import com.example.inventory_service.common.exception.ResourceNotFoundException;
import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.dto.DeductStockRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.entity.InventoryItem;
import com.example.inventory_service.mapper.InventoryMapper;
import com.example.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private InventoryItem mockItem;
    private InventoryResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockItem = new InventoryItem();
        mockItem.setProductId("PROD-1");
        mockItem.setQuantity(10);
        mockItem.setInStock(true);

        mockResponse = new InventoryResponse("PROD-1", 10, true, null);
    }

    @Test
    void getStockInfo_Found() {
        when(inventoryRepository.findByProductId("PROD-1")).thenReturn(Optional.of(mockItem));
        when(inventoryMapper.toResponse(mockItem)).thenReturn(mockResponse);

        InventoryResponse res = inventoryService.getStockInfo("PROD-1");

        assertNotNull(res);
        assertEquals("PROD-1", res.productId());
        assertEquals(10, res.quantity());
    }

    @Test
    void deductStock_Success() {
        when(inventoryRepository.findByProductId("PROD-1")).thenReturn(Optional.of(mockItem));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(mockItem);
        when(inventoryMapper.toResponse(mockItem)).thenReturn(mockResponse);

        DeductStockRequest req = new DeductStockRequest(5);
        InventoryResponse res = inventoryService.deductStock("PROD-1", req);

        assertNotNull(res);
        verify(inventoryRepository).save(mockItem);
        assertEquals(5, mockItem.getQuantity());
    }

    @Test
    void deductStock_InsufficientStock() {
        when(inventoryRepository.findByProductId("PROD-1")).thenReturn(Optional.of(mockItem));

        DeductStockRequest req = new DeductStockRequest(15);
        assertThrows(IllegalArgumentException.class, () -> inventoryService.deductStock("PROD-1", req));
    }
}
