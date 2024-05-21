package com.jolivan.archivomotorclasicobackend.Upvotes.Controllers.ExceptionControl.Exceptions;

public class UserAlredyUpvotedException extends RuntimeException {
    public UserAlredyUpvotedException() {
        super("User already upvoted");
    }
}
