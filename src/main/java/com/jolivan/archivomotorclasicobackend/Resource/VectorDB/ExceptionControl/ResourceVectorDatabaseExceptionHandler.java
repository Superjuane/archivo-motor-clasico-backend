package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl;

import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.ImageAlredyExistsException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.ResourceException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.VectorDatabaseNotWorkingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceVectorDatabaseExceptionHandler {

    @ExceptionHandler(value={ImageAlredyExistsException.class})
    public ResponseEntity<Object> handleImageAlredyExistsException(ImageAlredyExistsException e){
        HttpStatus status = HttpStatus.CONFLICT;

        String fullMessage = e.getMessage();
            String message = fullMessage.split(";")[0];
            String existingId = fullMessage.split(";")[1];

        ResourceException resourceException = new ResourceException(
                message,
                e,
                status,
                ZonedDateTime.now(),
                existingId
        );

        return new ResponseEntity<>(resourceException, status);
    }

    @ExceptionHandler(value={VectorDatabaseNotWorkingException.class})
    public ResponseEntity<Object> handleVectorDatabaseNotWorkingException(VectorDatabaseNotWorkingException e){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResourceException resourceException = new ResourceException(
                e.getMessage(),
                e,
                status,
                ZonedDateTime.now());

        return new ResponseEntity<>(resourceException, status);
    }
}
