package com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ParameterMissingExeption;
import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserNotAuthenticatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler(value={UserNotAuthenticatedException.class})
    public ResponseEntity<Object> handleUserNotAuthenticatedException(UserNotAuthenticatedException e){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Map<String, String> response = new java.util.HashMap<>();
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, status);
    }
}