package com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControll;

import com.jolivan.archivomotorclasicobackend.Resource.Controllers.ExeptionControll.Exeptions.ParameterMissingExeption;
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
}
