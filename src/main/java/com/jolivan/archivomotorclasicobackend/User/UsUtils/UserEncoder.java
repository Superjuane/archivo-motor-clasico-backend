package com.jolivan.archivomotorclasicobackend.User.UsUtils;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;

import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserEncoder {
    public static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserEncoder (){
        throw new IllegalStateException();
    }

    public static MyUser toEntity(final UserRequestDTO dto) {
        return MyUser.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .build();
    }

    public static UserResponseDTO toResponse(final MyUser entity) {
        return UserResponseDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole())
                .build();
    }
}
