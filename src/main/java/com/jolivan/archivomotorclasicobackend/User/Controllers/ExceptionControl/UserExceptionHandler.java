package com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl;

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
        if(e.getMessage().equals("User not registered")) response.put("problem","unregistered");
        if(e.getMessage().equals("Wrong password")) response.put("problem","wrongPassword");
        if(e.getMessage().equals("User already registered")) response.put("problem","userAlreadyRegistered");
        if(e.getMessage().equals("Email already registered")) response.put("problem","emailAlreadyRegistered");
        if(e.getMessage().equals("Email not valid")) response.put("problem","emailNotValid");
        return new ResponseEntity<>(response, status);
    }
}
