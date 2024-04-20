package com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions;

public class UserNodeNotFound extends RuntimeException {
    public UserNodeNotFound() {
        super("User not found");
    }
}
