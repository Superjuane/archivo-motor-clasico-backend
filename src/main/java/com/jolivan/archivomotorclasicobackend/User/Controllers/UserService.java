package com.jolivan.archivomotorclasicobackend.User.Controllers;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;
import com.jolivan.archivomotorclasicobackend.User.Utils.UserEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserResponseDTO create(final UserRequestDTO rq) {
        final MyUser user = UserEncoder.toEntity(rq);
        final MyUser result = this.repository.save(user);
        return UserEncoder.toResponse(result);
    }
}
