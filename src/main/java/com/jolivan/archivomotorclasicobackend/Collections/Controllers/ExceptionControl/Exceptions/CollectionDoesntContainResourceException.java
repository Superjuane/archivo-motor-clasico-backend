package com.jolivan.archivomotorclasicobackend.Collections.Controllers.ExceptionControl.Exceptions;

public class CollectionDoesntContainResourceException extends RuntimeException {
    public CollectionDoesntContainResourceException() {
        super("Collection doesn't contain resource");
    }
}
