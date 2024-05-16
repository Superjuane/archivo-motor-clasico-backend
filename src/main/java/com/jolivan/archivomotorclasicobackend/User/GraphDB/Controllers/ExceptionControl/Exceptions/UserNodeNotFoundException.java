package com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions;

public class UserNodeNotFoundException extends RuntimeException {
    public UserNodeNotFoundException() {
        super("User not found");
    }
}
