package com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers;

import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.ExceptionControl.Exceptions.UserNodeNotFoundException;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNodeService {

    private final UserNodeRepository userNodeRepository;

    @Autowired
    public UserNodeService(UserNodeRepository userNodeRepository) {
        this.userNodeRepository = userNodeRepository;
    }



    public List<UserNode> getAllUserNodes(){
        return userNodeRepository.findAll();
    }

    public UserNode createUser(UserNode newUser) {

        //TODO: not necessary to check, will do automatically when create (TODO: capture error)
        UserNode existingUser = userNodeRepository.findByUsername(newUser.getName());
        if(existingUser != null){
            throw new DataIntegrityViolationException("User already exists");
        }

        return userNodeRepository.save(newUser);
    }

    public UserNode getUserNodeByUsername(String username) throws UserNodeNotFoundException {
        UserNode user = userNodeRepository.findByUsername(username);
        if(user == null) throw new UserNodeNotFoundException();
        return user;
    }
}
