package com.example.order_service.service;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * OrderService interface.
 * Service interface defining business operations for Orders.
 */
public interface OrderService {
    /**
     * Places a new order and coordinates inventory deduction.
     * What it does: Creates a new order record, calculates total amounts, and sets the initial status.
     * Inter-service coordination: Needs to synchronously deduct stock for each requested item from the Inventory Service.
     * Invocation Order: Step 1 in the order lifecycle.
     *
     * @param request The order details including customer ID and items
     * @return OrderResponse containing the created order ID and details
     */
    OrderResponse placeOrder(CreateOrderRequest request);

    /**
     * Retrieves the current status and details of a specific order.
     * What it does: Fetches an order by its unique ID. Throws ResourceNotFoundException if it doesn't exist.
     * Invocation Order: Independent. Used by customers or internal systems to track order state.
     *
     * @param orderId The unique identifier of the order
     * @return OrderResponse containing current order state
     */
    OrderResponse getOrderStatus(String orderId);

    /**
     * Retrieves a paginated list of all orders.
     * What it does: Returns a page of orders for administrative or historical viewing.
     * Invocation Order: Independent.
     *
     * @param pageable Pagination and sorting configuration
     * @return A paginated list of OrderResponse objects
     */
    Page<OrderResponse> listOrders(Pageable pageable);

    /**
     * Cancels an existing order and coordinates inventory restoration.
     * What it does: Updates the status of an eligible order to 'CANCELLED'. Prevents cancellation if already shipped.
     * Inter-service coordination: Needs to synchronously restore the previously deducted stock to the Inventory Service.
     * Invocation Order: Step 4 in the order lifecycle (terminal state).
     *
     * @param orderId The unique identifier of the order to cancel
     * @return OrderResponse reflecting the cancelled state
     */
    OrderResponse cancelOrder(String orderId);
}
