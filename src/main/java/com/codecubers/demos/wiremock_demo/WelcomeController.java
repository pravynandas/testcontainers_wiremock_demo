package com.codecubers.demos.wiremock_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks this class as a REST Controller
public class WelcomeController {

    @GetMapping("/welcome") // Maps HTTP GET requests to /welcome to this method
    public String welcome() {
        return "Welcome to the Spring Boot REST API!";
    }
}