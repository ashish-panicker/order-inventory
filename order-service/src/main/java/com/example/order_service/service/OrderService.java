package com.example.order_service.service;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse placeOrder(CreateOrderRequest request);
    OrderResponse getOrderStatus(String orderId);
    Page<OrderResponse> listOrders(Pageable pageable);
    OrderResponse cancelOrder(String orderId);
}
