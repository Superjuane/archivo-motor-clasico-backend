package com.jolivan.archivomotorclasicobackend.Comments.Controllers.ExceptionControl.Exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("Comment not found");
    }
}
