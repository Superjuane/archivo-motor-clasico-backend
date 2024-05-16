package com.jolivan.archivomotorclasicobackend.User.Controllers.service;

import com.jolivan.archivomotorclasicobackend.User.Entities.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUserId(Long id);
}
