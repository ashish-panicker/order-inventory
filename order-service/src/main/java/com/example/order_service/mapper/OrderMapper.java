package com.example.order_service.mapper;

import com.example.order_service.dto.OrderResponse;
import com.example.order_service.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for transforming {@link Order} entities into {@link OrderResponse} DTOs.
 * Uses MapStruct to generate the implementation class at compile time, configured as a Spring component.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Converts an {@link Order} entity into an {@link OrderResponse}.
     * <p>
     * The {@code createdAt} field is formatted into an ISO-8601 string representation.
     * </p>
     *
     * @param order the {@link Order} entity to be converted
     * @return the mapped {@link OrderResponse} DTO
     */
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    OrderResponse toResponse(Order order);
}
