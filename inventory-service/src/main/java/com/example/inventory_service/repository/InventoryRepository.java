package com.example.inventory_service.repository;

import com.example.inventory_service.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * InventoryRepository interface.
 * Repository interface for database operations on Inventory entities.
 */
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByProductId(String productId);
}
