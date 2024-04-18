package com.jolivan.archivomotorclasicobackend.User.Controllers.service.impl;

import com.jolivan.archivomotorclasicobackend.User.Controllers.UserRepository;
import com.jolivan.archivomotorclasicobackend.User.Controllers.service.UserService;
import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import com.jolivan.archivomotorclasicobackend.User.UUtils.UserEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserResponseDTO create(final UserRequestDTO rq) {
        final MyUser newUser = UserEncoder.toEntity(rq);
        if(newUser.getUsername().equals("admin")){newUser.setRole("ROLE_ADMIN");}else{newUser.setRole("ROLE_USER");}
        final MyUser result = this.repository.save(newUser);
        return UserEncoder.toResponse(result);
    }
}
