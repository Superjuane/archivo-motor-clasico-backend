package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions;

public class IdIsNullException extends RuntimeException {
    public IdIsNullException(String message) {
        super(message);
    }

    public IdIsNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
