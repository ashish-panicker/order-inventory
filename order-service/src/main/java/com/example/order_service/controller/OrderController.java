package com.example.order_service.controller;

import com.example.order_service.common.dto.ApiResponse;
import com.example.order_service.common.dto.PaginatedResponse;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order management APIs")
/**
 * OrderController class.
 * REST Controller exposing API endpoints for managing Orders.
 */
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create order", description = "Creates a new order")
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderService.placeOrder(request));
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details", description = "Retrieves details of an order by ID")
    public ApiResponse<OrderResponse> getOrderDetails(@PathVariable String orderId) {
        return ApiResponse.success(orderService.getOrderStatus(orderId));
    }

    /**
     * Retrieves a paginated and sortable list of orders.
     * Clients can pass query parameters like ?page=0&size=10&sort=createdAt,desc to control pagination and filtering.
     * Spring injects the Pageable instance populated with these parameters automatically.
     */
    @GetMapping
    @Operation(summary = "List orders", description = "Retrieves a paginated list of orders")
    public PaginatedResponse<OrderResponse> listOrders(Pageable pageable) {
        Page<OrderResponse> page = orderService.listOrders(pageable);
        return PaginatedResponse.of(page);
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order", description = "Cancels an existing order")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return ApiResponse.success(orderService.cancelOrder(orderId));
    }
}
