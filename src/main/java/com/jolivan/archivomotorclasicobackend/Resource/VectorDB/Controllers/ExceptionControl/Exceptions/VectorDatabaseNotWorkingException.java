package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions;

public class VectorDatabaseNotWorkingException extends RuntimeException {
    public VectorDatabaseNotWorkingException(String message) {
        super(message);
    }

    public VectorDatabaseNotWorkingException(String message, Throwable cause) {
        super(message, cause);
    }
}
