package com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ExceptionControl;

public class ResourceNodeNotFoundException extends RuntimeException {
    public ResourceNodeNotFoundException() {
        super("Resource node not found");
    }
}
