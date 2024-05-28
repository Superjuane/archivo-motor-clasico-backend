package com.jolivan.archivomotorclasicobackend.User.Controllers.service;


import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.Entities.PasswordResetToken;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {



    public void createPasswordResetTokenForUser(MyUser user, String token);

    UserResponseDTO create(final UserRequestDTO rq);

    UserResponseDTO authenticate(UserRequestDTO request);

    MyUser findUserByUsername(String username);

    UserResponseDTO findUserByUsernameRestricted(String username);


    MyUser findUserByEmail(String email);

    PasswordResetToken getToken(String token);

    PasswordResetToken getTokenByUserId(Long id);

    void updateToken(PasswordResetToken tokenEntity);

    void updateUserPassword(MyUser user, String password);

    void deleteTokenFromUser(Long id);
}
