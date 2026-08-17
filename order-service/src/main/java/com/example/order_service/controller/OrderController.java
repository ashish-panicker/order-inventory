package com.example.order_service.controller;

import com.example.order_service.common.dto.ApiResponse;
import com.example.order_service.common.dto.PaginatedResponse;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ApiResponse.success(orderService.placeOrder(request));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderResponse> getOrderDetails(@PathVariable String orderId) {
        return ApiResponse.success(orderService.getOrderStatus(orderId));
    }

    @GetMapping
    public PaginatedResponse<OrderResponse> listOrders(Pageable pageable) {
        Page<OrderResponse> page = orderService.listOrders(pageable);
        return PaginatedResponse.of(page);
    }

    @PutMapping("/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return ApiResponse.success(orderService.cancelOrder(orderId));
    }
}
