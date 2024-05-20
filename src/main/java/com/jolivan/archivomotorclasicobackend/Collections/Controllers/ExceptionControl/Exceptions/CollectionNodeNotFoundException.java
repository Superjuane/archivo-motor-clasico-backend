package com.jolivan.archivomotorclasicobackend.Collections.Controllers.ExceptionControl.Exceptions;

public class CollectionNodeNotFoundException extends RuntimeException {
    public CollectionNodeNotFoundException() {
        super("Collection node not found");
    }
}
