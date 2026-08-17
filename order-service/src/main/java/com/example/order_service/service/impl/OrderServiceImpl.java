package com.example.order_service.service.impl;

import com.example.order_service.common.exception.ResourceNotFoundException;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
/**
 * OrderServiceImpl class.
 * Implementation of the business logic for Order management.
 */
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest request) {
        // NOTE: Stub for Inter-Service call to deduct stock.
        // Assuming stock deduction succeeds for now.

        List<OrderItemRequest> orderItemRequestList = request.items();


        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerId(request.customerId());
        order.setStatus("CONFIRMED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (var itemReq : request.items()) {
            OrderItem item = new OrderItem();
            item.setProductId(itemReq.productId());
            item.setQuantity(itemReq.quantity());
            item.setPrice(itemReq.price());
            order.addItem(item);
            
            BigDecimal itemTotal = itemReq.price().multiply(BigDecimal.valueOf(itemReq.quantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        order.setTotalAmount(totalAmount);
        
        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Override
    public OrderResponse getOrderStatus(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID does not exist: " + orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    public Page<OrderResponse> listOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order ID does not exist: " + orderId));
                
        if ("SHIPPED".equals(order.getStatus())) {
            throw new IllegalArgumentException("Order already shipped and cannot be cancelled.");
        }
        
        order.setStatus("CANCELLED");
        // NOTE: Stub for inventory restoration
        
        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }
}
