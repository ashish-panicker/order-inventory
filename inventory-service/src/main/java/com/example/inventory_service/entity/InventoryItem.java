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
@SQLDelete(sql = "UPDATE inventory_items SET is_deleted = true WHERE id=?")
@SQLRestriction("is_deleted=false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private Integer quantity;

    private boolean inStock;

    private boolean isDeleted = false;
}
