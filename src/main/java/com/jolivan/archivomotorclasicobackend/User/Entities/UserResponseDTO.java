package com.jolivan.archivomotorclasicobackend.User.Entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String username;
    private String email;
    private String role;
}

