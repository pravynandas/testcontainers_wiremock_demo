package com.codecubers.demos.wiremock_demo.services;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

    public String sayHello(String name) {
        if (name.isBlank()) {
            return "Hello, Missing!!";
        }
        return "Hello, " + name + "!!";
    }

}
