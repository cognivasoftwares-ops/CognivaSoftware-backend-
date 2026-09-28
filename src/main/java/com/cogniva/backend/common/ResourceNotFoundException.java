package com.cogniva.backend.common;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object key) {
        super(resource + " not found: " + key);
    }
}
