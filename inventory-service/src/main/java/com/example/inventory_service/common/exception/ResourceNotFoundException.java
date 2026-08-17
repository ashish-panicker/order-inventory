package com.example.inventory_service.common.exception;

/**
 * ResourceNotFoundException class.
 * Custom exception class for ResourceNotFound errors.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
