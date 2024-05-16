package com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Resource not found");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
