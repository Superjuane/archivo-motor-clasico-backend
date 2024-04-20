package com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException(String userNotRegistered) {
        super("User not authenticated");
    }
}
