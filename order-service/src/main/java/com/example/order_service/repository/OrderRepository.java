package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * OrderRepository interface.
 * Repository interface for database operations on Order entities.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);
}
