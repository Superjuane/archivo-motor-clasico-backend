package com.jolivan.archivomotorclasicobackend.User.Controllers;

import com.jolivan.archivomotorclasicobackend.User.Entities.MyUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<MyUser,Long> {
    MyUser findByUsername(final String username);

    MyUser findByEmail(String email);
}