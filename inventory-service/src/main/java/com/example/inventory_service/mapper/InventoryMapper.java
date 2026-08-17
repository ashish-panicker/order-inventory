package com.example.inventory_service.mapper;

import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.entity.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for transforming {@link InventoryItem} entities into {@link InventoryResponse} DTOs.
 * Uses MapStruct to generate the implementation class at compile time, configured as a Spring component.
 */
@Mapper(componentModel = "spring")
public interface InventoryMapper {

    /**
     * Converts an {@link InventoryItem} entity into an {@link InventoryResponse}.
     * <p>
     * The {@code updatedAt} field is explicitly mapped to {@code lastUpdated}
     * and formatted into an ISO-8601 string representation.
     * </p>
     *
     * @param inventoryItem the {@link InventoryItem} entity to be converted
     * @return the mapped {@link InventoryResponse} DTO
     */
    @Mapping(target = "lastUpdated", source = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    InventoryResponse toResponse(InventoryItem inventoryItem);
}
