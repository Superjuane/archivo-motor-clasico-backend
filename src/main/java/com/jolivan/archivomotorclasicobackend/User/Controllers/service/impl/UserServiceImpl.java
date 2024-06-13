package com.jolivan.archivomotorclasicobackend.User.Controllers.service.impl;

import com.jolivan.archivomotorclasicobackend.User.Controllers.ExceptionControl.Exceptions.UserNotAuthenticatedException;
import com.jolivan.archivomotorclasicobackend.User.Controllers.UserRepository;
import com.jolivan.archivomotorclasicobackend.User.Controllers.service.PasswordResetTokenRepository;
import com.jolivan.archivomotorclasicobackend.User.Controllers.service.UserService;
import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.Entities.PasswordResetToken;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.UserNodeService;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import com.jolivan.archivomotorclasicobackend.User.UsUtils.UserEncoder;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import static io.netty.util.CharsetUtil.encoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserNodeService userNodeService;

    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;

    @Override
    public UserResponseDTO create(final UserRequestDTO rq) {
        final MyUser newUser = UserEncoder.toEntity(rq);

        if(!isValidEmailAddress(rq.getEmail())) throw new UserNotAuthenticatedException("Email not valid");

        if(newUser.getUsername().equals("admin")){newUser.setRole("ROLE_ADMIN");}else{newUser.setRole("ROLE_USER");}

        MyUser existingUser = repository.findByUsername(newUser.getUsername());
        if(existingUser != null){
            throw new UserNotAuthenticatedException("User already registered");
        }

        existingUser = repository.findByEmail(newUser.getEmail());
        if(existingUser != null){
            throw new UserNotAuthenticatedException("Email already registered");
        }

        //save user in PostgreSQL
        final MyUser result = this.repository.save(newUser);

        //save user in Neo4j
        UserNode newUserNode = UserNode.builder()
                .name(newUser.getUsername())
                .build();
        userNodeService.createUser(newUserNode);

        return UserEncoder.toResponse(result);
    }

    @Override
    public UserResponseDTO authenticate(UserRequestDTO request) {
        MyUser user = repository.findByUsername(request.getUsername());
        if(user == null){
            throw new UserNotAuthenticatedException("User not registered");
        }
        if(UserEncoder.encoder.matches(request.getPassword(), user.getPassword())){
            return UserEncoder.toResponse(user);
        }
        throw new UserNotAuthenticatedException("Wrong password");
    }

    @Override
    public MyUser findUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public UserResponseDTO findUserByUsernameRestricted(String username) {
        MyUser user = repository.findByUsername(username);
        UserResponseDTO u = UserEncoder.toResponse(user);
        return u;
    }

    @Override
    public MyUser findUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void createPasswordResetTokenForUser(MyUser user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public PasswordResetToken getToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public PasswordResetToken getTokenByUserId(Long id) {
        PasswordResetToken x = passwordTokenRepository.findByUserId(id);
        return x;
    }

    @Override
    public void updateToken(PasswordResetToken tokenEntity) {
        passwordTokenRepository.save(tokenEntity);
    }

    @Override
    public void updateUserPassword(MyUser user, String password) {
        user.setPassword(UserEncoder.encoder.encode(password));
        repository.save(user);
    }

    @Override
    public void deleteTokenFromUser(Long id) {
        passwordTokenRepository.deleteByUserId(id);
    }


    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
