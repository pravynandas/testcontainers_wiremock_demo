package com.codecubers.demos.wiremock_demo;

import com.codecubers.demos.wiremock_demo.services.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ServiceController {

    final HelloService service;

    public ServiceController(HelloService service) {
        this.service = service;
    }

    @GetMapping("/hello")
    public @ResponseBody String sayHello(@RequestParam(value = "name", defaultValue = "Unknown") String name) {
        return service.sayHello(name);
    }

    @GetMapping("/users/uid")
    public String extractUserId(@PathVariable String uId) {
        return "User ID: " + uId;
    }


}
