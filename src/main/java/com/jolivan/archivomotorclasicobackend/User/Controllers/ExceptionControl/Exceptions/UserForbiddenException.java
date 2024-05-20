package com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions;

public class UserForbiddenException extends RuntimeException {
    public UserForbiddenException() {
        super("User not authorized");
    }
}
