package com.example.order_service.controller;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderItemRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.service.OrderService;

import tools.jackson.databind.json.JsonMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    void createOrder_ReturnsCreated() throws Exception {
        OrderResponse mockResponse = new OrderResponse("ORD-123", "CONFIRMED", BigDecimal.valueOf(100), "2023-10-01T10:00:00Z");
        CreateOrderRequest request = new CreateOrderRequest("CUST-1", List.of(
                new OrderItemRequest("PROD-1", 2, BigDecimal.valueOf(50))
        ));

        when(orderService.placeOrder(any(CreateOrderRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value("ORD-123"));
    }

    @Test
    void getOrderDetails_ReturnsSuccess() throws Exception {
        OrderResponse mockResponse = new OrderResponse("ORD-123", "CONFIRMED", BigDecimal.valueOf(100), "2023-10-01T10:00:00Z");
        when(orderService.getOrderStatus("ORD-123")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/orders/ORD-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.orderId").value("ORD-123"));
    }

    @Test
    void listOrders_ReturnsPaginated() throws Exception {
        OrderResponse mockResponse = new OrderResponse("ORD-123", "CONFIRMED", BigDecimal.valueOf(100), "2023-10-01T10:00:00Z");
        when(orderService.listOrders(any())).thenReturn(new PageImpl<>(List.of(mockResponse), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/v1/orders?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].orderId").value("ORD-123"))
                .andExpect(jsonPath("$.meta.totalElements").value(1));
    }

    @Test
    void cancelOrder_ReturnsSuccess() throws Exception {
        OrderResponse mockResponse = new OrderResponse("ORD-123", "CANCELLED", BigDecimal.valueOf(100), "2023-10-01T10:00:00Z");
        when(orderService.cancelOrder("ORD-123")).thenReturn(mockResponse);

        mockMvc.perform(put("/api/v1/orders/ORD-123/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }
}
