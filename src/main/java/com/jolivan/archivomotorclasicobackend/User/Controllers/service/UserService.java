package com.jolivan.archivomotorclasicobackend.User.Controllers.service;


import com.jolivan.archivomotorclasicobackend.User.Entities.UserRequestDTO;
import com.jolivan.archivomotorclasicobackend.User.Entities.UserResponseDTO;

public interface UserService {
    UserResponseDTO create(final UserRequestDTO rq);
}
