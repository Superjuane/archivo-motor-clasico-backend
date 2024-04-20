package com.jolivan.archivomotorclasicobackend.User.UUtils;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;

import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

public class UserEncoder {
    public static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private UserEncoder (){
        throw new IllegalStateException();
    }

    public static MyUser toEntity(final UserRequestDTO dto) {
        String password = encoder.encode(dto.getPassword());
        String password2 = encoder.encode(dto.getPassword());
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
