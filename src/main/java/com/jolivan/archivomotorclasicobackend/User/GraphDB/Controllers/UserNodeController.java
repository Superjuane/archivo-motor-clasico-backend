package com.jolivan.archivomotorclasicobackend.User.GraphDB.Controllers;

import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Controllers.ResourceNodeService;
import com.jolivan.archivomotorclasicobackend.Resource.GraphDB.Entities.ResourceNode;
import com.jolivan.archivomotorclasicobackend.User.GraphDB.Entities.UserNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequestMapping("resourcenode")
public class UserNodeController {

    private final UserNodeService userNodeService;

    @Autowired
    public UserNodeController(UserNodeService userNodeService) {
        this.userNodeService = userNodeService;
    }

    @GetMapping("/usersnodes")
    public List<UserNode> getAllResourceNodes() {
        return userNodeService.getAllUserNodes();
    }
}
