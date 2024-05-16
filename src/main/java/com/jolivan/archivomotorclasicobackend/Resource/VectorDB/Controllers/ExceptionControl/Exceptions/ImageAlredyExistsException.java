package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions;

public class ImageAlredyExistsException extends RuntimeException {
    public ImageAlredyExistsException(String message) {
        super(message);
    }

    public ImageAlredyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageAlredyExistsException(String message, String existingId) {
        super(message + ";" + existingId);
    }
}
