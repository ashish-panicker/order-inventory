package com.example.order_service.service.impl;

import com.example.order_service.common.exception.ResourceNotFoundException;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
/**
 * OrderServiceImplTest class.
 * Unit tests for the Order service implementation.
 */

// mvn clean install -DskipTests
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order mockOrder;
    private OrderResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockOrder = new Order();
        mockOrder.setOrderId("ORD-123");
        mockOrder.setStatus("CONFIRMED");

        mockResponse = new OrderResponse("ORD-123", "CONFIRMED", BigDecimal.TEN, null);
    }

    @Test
    void placeOrder_Success() {
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(mockResponse);

        CreateOrderRequest req = new CreateOrderRequest("CUST-1", List.of(
                new OrderItemRequest("PROD-1", 1, BigDecimal.TEN)
        ));

        OrderResponse res = orderService.placeOrder(req);

        assertNotNull(res);
        assertEquals("ORD-123", res.orderId());
    }

    @Test
    void cancelOrder_Success() {
        when(orderRepository.findByOrderId("ORD-123")).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(mockResponse);

        OrderResponse res = orderService.cancelOrder("ORD-123");

        assertNotNull(res);
        assertEquals("CANCELLED", mockOrder.getStatus());
        verify(orderRepository).save(mockOrder);
    }
}
