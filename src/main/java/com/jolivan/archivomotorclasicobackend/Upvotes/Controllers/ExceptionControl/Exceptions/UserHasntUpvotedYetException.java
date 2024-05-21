package com.jolivan.archivomotorclasicobackend.Upvotes.Controllers.ExceptionControl.Exceptions;

public class UserHasntUpvotedYetException extends RuntimeException {
    public UserHasntUpvotedYetException() {
        super("User hasn't upvoted yet");
    }
}
