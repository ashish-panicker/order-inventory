package com.example.inventory_service.controller;

import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.dto.DeductStockRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.service.InventoryService;

import tools.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryService inventoryService;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void getStockInfo_ReturnsSuccess() throws Exception {
        InventoryResponse mockResponse = new InventoryResponse("PROD-1", 10, true, "2023-10-01T10:00:00Z");
        when(inventoryService.getStockInfo("PROD-1")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/inventory/PROD-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.productId").value("PROD-1"))
                .andExpect(jsonPath("$.data.quantity").value(10));
    }

    @Test
    void listInventory_ReturnsPaginated() throws Exception {
        InventoryResponse mockResponse = new InventoryResponse("PROD-1", 10, true, "2023-10-01T10:00:00Z");
        when(inventoryService.listInventory(any())).thenReturn(new PageImpl<>(List.of(mockResponse), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/inventory?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].productId").value("PROD-1"))
                .andExpect(jsonPath("$.meta.totalElements").value(1));
    }

    @Test
    void addStock_ReturnsCreated() throws Exception {
        InventoryResponse mockResponse = new InventoryResponse("PROD-1", 15, true, "2023-10-01T10:00:00Z");
        AddStockRequest request = new AddStockRequest("PROD-1", 5);
        
        when(inventoryService.addStock(any(AddStockRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.quantity").value(15));
    }

    @Test
    void deductStock_ReturnsSuccess() throws Exception {
        InventoryResponse mockResponse = new InventoryResponse("PROD-1", 5, true, "2023-10-01T10:00:00Z");
        DeductStockRequest request = new DeductStockRequest(5);

        when(inventoryService.deductStock(eq("PROD-1"), any(DeductStockRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/inventory/PROD-1/deduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.quantity").value(5));
    }
}
