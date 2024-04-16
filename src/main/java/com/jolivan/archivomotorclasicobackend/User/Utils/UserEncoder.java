package com.jolivan.archivomotorclasicobackend.User.Utils;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;

import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserEncoder {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static MyUser toEntity(final UserRequestDTO dto) {
        return MyUser.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .build();
    }

    public static UserResponseDTO toResponse(final MyUser entity) {
        return UserResponseDTO.builder()
                .username(entity.getUsername())
                .build();
    }
}
