package com.example.order_service.entity;

import com.example.order_service.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
// Enables soft delete: overrides the default DELETE SQL to update the is_deleted flag instead.
// This preserves historical data and avoids physical deletion from the database.
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE id=?")
// Automatically applies a WHERE clause to all SELECT queries to filter out soft-deleted records.
// This ensures that deleted orders are hidden from standard application queries.
@SQLRestriction("is_deleted=false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/**
 * Order class.
 * Represents a Order entity in the domain model.
 */
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String orderId;

    private String customerId;

    private String status;

    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private boolean isDeleted = false;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
