package com.example.inventory_service.service;

import com.example.inventory_service.dto.AddStockRequest;
import com.example.inventory_service.dto.DeductStockRequest;
import com.example.inventory_service.dto.RestoreStockRequest;
import com.example.inventory_service.dto.UpdateStockRequest;
import com.example.inventory_service.dto.InventoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * InventoryService interface.
 * Service interface defining business operations for Inventorys.
 */
public interface InventoryService {
    /**
     * Retrieves the current stock level and details for a specific product.
     * What it does: Fetches a single inventory item by its productId.
     * How it works: Queries the database for the given product ID. Throws a ResourceNotFoundException if the item does not exist.
     * Invocation Order: Step 2. Typically invoked to verify stock or display details after initialization.
     *
     * @param productId The unique identifier of the product
     * @return InventoryResponse containing the current stock details
     */
    InventoryResponse getStockInfo(String productId);

    /**
     * Retrieves a paginated list of all inventory items.
     * What it does: Returns a page of inventory records.
     * How it works: Uses Spring Data JPA's Pageable to query the repository and map the results to InventoryResponse DTOs.
     * Invocation Order: Step 3. Used to view the overall state of the inventory system at any time.
     *
     * @param pageable Pagination and sorting information
     * @return A page of InventoryResponse objects
     */
    Page<InventoryResponse> listInventory(Pageable pageable);

    /**
     * Initializes or adds new stock for a product.
     * What it does: Creates a new inventory record if the product doesn't exist, or adds to the existing stock quantity.
     * How it works: Checks if the product exists. If not, initializes it with 0 quantity. Then adds the requested quantity and updates the 'inStock' status.
     * Invocation Order: Step 1 in the lifecycle. Usually the first method invoked to introduce a product into the system.
     *
     * @param request AddStockRequest containing the productId and quantityToAdd
     * @return InventoryResponse with the updated stock details
     */
    InventoryResponse addStock(AddStockRequest request);

    /**
     * Deducts quantity from a product's stock.
     * What it does: Reduces the available quantity of a product by the specified amount.
     * How it works: Finds the product. Validates if the current quantity is sufficient. If sufficient, subtracts the quantity and updates the 'inStock' status. Throws an exception if stock is insufficient.
     * Invocation Order: Step 5 in the logical lifecycle. Typically invoked synchronously by the Order Service when a new order is placed to reserve stock.
     *
     * @param productId The unique identifier of the product
     * @param request DeductStockRequest containing the quantity to deduct
     * @return InventoryResponse with the updated stock details
     */
    InventoryResponse deductStock(String productId, DeductStockRequest request);

    /**
     * Restores stock quantity for a product.
     * What it does: Adds stock back to the inventory, typically to reverse a prior deduction.
     * How it works: Finds the product (or creates it if missing), adds the requested quantity back, and updates the 'inStock' status.
     * Invocation Order: Step 6 in the logical lifecycle. Typically invoked by the Order Service when an existing order is cancelled to release reserved stock.
     *
     * @param productId The unique identifier of the product
     * @param request RestoreStockRequest containing the quantity to restore
     * @return InventoryResponse with the updated stock details
     */
    InventoryResponse restoreStock(String productId, RestoreStockRequest request);

    /**
     * Adds additional quantity to an existing product's stock via a path variable update.
     * What it does: Increases the stock level of a product.
     * How it works: Finds the product (or initializes it if missing), adds the specified quantity, and sets 'inStock' appropriately.
     * Invocation Order: Step 4 in the logical lifecycle. Used for manual stock replenishment independently of the Order Service.
     *
     * @param productId The unique identifier of the product
     * @param request UpdateStockRequest containing the quantity to add
     * @return InventoryResponse with the updated stock details
     */
    InventoryResponse addStockQuantity(String productId, UpdateStockRequest request);

    /**
     * Removes a product completely from the inventory system.
     * What it does: Deletes the inventory record associated with the given product ID.
     * How it works: Queries the product. If it exists, it deletes the record from the database. Throws ResourceNotFoundException if missing.
     * Invocation Order: Step 7 in the logical lifecycle. The final step when a product is discontinued and completely removed.
     *
     * @param productId The unique identifier of the product
     */
    void deleteInventory(String productId);
}
