package com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ParameterMissingExeption;
import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControl.Exeptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ResourceExeptionHandler {
    @ExceptionHandler(value={ParameterMissingExeption.class})
    public ResponseEntity<Object> handleParameterMissingExeption(ParameterMissingExeption e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> response = new java.util.HashMap<>();
        response.put("message", e.getMessage());
        response.put("parameters", String.join(", ", e.getParameters()));
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value={ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, String> response = new java.util.HashMap<>();
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, status);
    }

}
