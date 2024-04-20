package com.jolivan.archivomotorclasicobackend.User.Controllers;

import com.jolivan.archivomotorclasicobackend.User.Controllers.service.UserService;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private static final String URL = "http://localhost:3000";

    private final UserService service;

    @CrossOrigin(origins = URL)
    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(final @Valid @RequestBody UserRequestDTO rq) {
        return new ResponseEntity<>(this.service.create(rq), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = URL)
    @PostMapping("/user/authenticate")
    public ResponseEntity<UserResponseDTO> authenticateUser(final @Valid @RequestBody UserRequestDTO rq) {
        UserResponseDTO response = this.service.authenticate(rq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
