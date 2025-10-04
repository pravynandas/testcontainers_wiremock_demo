package com.codecubers.demos.wiremock_demo;

import com.codecubers.demos.wiremock_demo.services.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ServiceControllerTest {

    @Autowired
    ServiceController controller;

    @Test
    public void hello() {
        assertThat(controller).isNotNull();
        assertThat(controller.sayHello("")).isEqualTo("Hello, Missing!!");
        assertThat(controller.sayHello("Tom")).isEqualTo("Hello, Tom!!");
    }

}