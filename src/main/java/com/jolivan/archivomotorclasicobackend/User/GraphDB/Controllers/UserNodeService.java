package com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers;

import com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers.UserNodeRepository;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
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
}
