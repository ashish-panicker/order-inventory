package com.example.inventory_service.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
    boolean success,
    List<T> data,
    PageMeta meta
) {
    public static <T> PaginatedResponse<T> of(Page<T> page) {
        return new PaginatedResponse<>(
            true,
            page.getContent(),
            new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSort().toString()
            )
        );
    }

    public record PageMeta(
        int page,
        int size,
        long totalElements,
        int totalPages,
        String sort
    ) {}
}
