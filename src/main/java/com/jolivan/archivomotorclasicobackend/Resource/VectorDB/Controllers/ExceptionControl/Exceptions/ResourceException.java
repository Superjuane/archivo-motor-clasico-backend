package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ExceptionControl.Exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;

public class ResourceException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus status;
    private final ZonedDateTime timestamp;

    private String id;


    public ResourceException(String message, Throwable throwable, HttpStatus status, ZonedDateTime timestamp) {
        this.message = message;
        this.throwable = throwable;
        this.status = status;
        this.timestamp = timestamp;
    }
    public ResourceException(String message, Throwable throwable, HttpStatus status, ZonedDateTime timestamp, String id) {
        this.message = message;
        this.throwable = throwable;
        this.status = status;
        this.timestamp = timestamp;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getId() {return id;}
}
