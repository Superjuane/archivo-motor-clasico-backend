package com.jolivan.archivomotorclasicobackend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloWorldController {
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/sayhello")
    public Map<String, String> sayHelloWorld() {
        Map<String, String> response = new HashMap<>();
        response.put("result", "-prove that the backend is working-");
        return response;
    }
}