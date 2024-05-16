package com.jolivan.archivomotorclasicobackend.User.Controllers;

import com.jolivan.archivomotorclasicobackend.User.Controllers.service.UserService;
import com.jolivan.archivomotorclasicobackend.User.Entities.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;


import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final String URL = "http://localhost:3000";

    private final UserService service;


    @Autowired
    private MessageSource messages;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment env;

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

    @CrossOrigin(origins = URL)
    @PostMapping("/user/forgotpassword")
    public ResponseEntity<Object> forgotPassword(@RequestParam("email") final String email) {
        final MyUser user = service.findUserByEmail(email);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            try {
                service.createPasswordResetTokenForUser(user, token);
            } catch (DataIntegrityViolationException e) {
                e.getStackTrace();
                if(e.getCause().getMessage().contains("llave duplicada")){
                    PasswordResetToken tokenEntity = service.getTokenByUserId(user.getId());
                    tokenEntity.setToken(token);
                    tokenEntity.setExpiryDate(Date.from(tokenEntity.getExpiryDate().toInstant().plusSeconds(86400)));
                    service.updateToken(tokenEntity);
                }
            }
            SimpleMailMessage mail = constructResetTokenEmail(token, user);
            System.out.println(token);
            System.out.println("Sending email for token: http://localhost:3000/resetpassword?token="+token);
            mailSender.send(mail);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = URL)
    @PostMapping("/user/resetpassword")
    public ResponseEntity<Object> resetPassword(@RequestBody final NewPasswordDTO dto) {
        //TODO: migrate all logic to other layers (i.e. service)
        System.out.println(dto.getToken());

        final PasswordResetToken token = service.getToken(dto.getToken());

        if (token == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else if(token.getExpiryDate().before(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)))){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final MyUser user = token.getUser();
        service.updateUserPassword(user, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private SimpleMailMessage constructResetTokenEmail(String token, MyUser user) {
        String url = "http://localhost:3000/resetpassword?token=" + token+"\n";
        String message = "Renueva tu contraseña";
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body,
                                             MyUser user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("archivomotorclasico@gmail.com");
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        return email;
    }
}


