package com.example.inventory_service.entity;

import com.example.inventory_service.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "inventory_items")
// Enables soft delete: overrides the default DELETE SQL to update the is_deleted flag instead.
// This preserves historical data and avoids physical deletion from the database.
@SQLDelete(sql = "UPDATE inventory_items SET is_deleted = true WHERE id=?")
// Automatically applies a WHERE clause to all SELECT queries to filter out soft-deleted records.
// This ensures that deleted items are hidden from standard application queries.
@SQLRestriction("is_deleted=false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * InventoryItem class.
 * Represents a InventoryItem entity in the domain model.
 */
public class InventoryItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private Integer quantity;

    private boolean inStock;

    private boolean isDeleted = false;
}
