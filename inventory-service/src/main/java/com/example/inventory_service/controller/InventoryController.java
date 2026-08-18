package com.example.inventory_service.controller;

import com.example.inventory_service.common.dto.ApiResponse;
import com.example.inventory_service.common.dto.PaginatedResponse;
import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.dto.DeductStockRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management APIs")
/**
 * InventoryController class.
 * REST Controller exposing API endpoints for managing Inventorys.
 */
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    @Operation(summary = "Get stock info", description = "Retrieves stock information for a given product ID")
    public ApiResponse<InventoryResponse> getStockInfo(@PathVariable String productId) {
        return ApiResponse.success(inventoryService.getStockInfo(productId));
    }

    /**
     * Retrieves a paginated and sortable list of inventory items.
     * Spring Data web support automatically translates request parameters (like page=0&size=20&sort=productId,asc)
     * into a Pageable object, providing out-of-the-box pagination and sorting capabilities.
     */
    @GetMapping
    @Operation(summary = "List inventory", description = "Retrieves a paginated list of inventory items")
    public PaginatedResponse<InventoryResponse> listInventory(Pageable pageable) {
        Page<InventoryResponse> page = inventoryService.listInventory(pageable);
        return PaginatedResponse.of(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add stock", description = "Adds new stock for a product")
    public ApiResponse<InventoryResponse> addStock(@Valid @RequestBody AddStockRequest request) {
        return ApiResponse.success(inventoryService.addStock(request));
    }

    @PutMapping("/{productId}/deduct")
    @Operation(summary = "Deduct stock", description = "Deducts stock for a product")
    public ApiResponse<InventoryResponse> deductStock(
            @PathVariable String productId,
            @Valid @RequestBody DeductStockRequest request) {
        return ApiResponse.success(inventoryService.deductStock(productId, request));
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete inventory", description = "Deletes inventory record for a product")
    public ApiResponse<Void> deleteInventory(@PathVariable String productId) {
        inventoryService.deleteInventory(productId);
        return ApiResponse.success(null, "Inventory deleted successfully");
    }
}
