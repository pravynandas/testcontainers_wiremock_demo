package com.codecubers.demos.wiremock_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WelcomeControllerTest {

    @Autowired
    private WelcomeController controller;

    @Test
    void welcome() {
        assertThat(controller).isNotNull();
        assertThat(controller.welcome()).isEqualTo("Welcome to the Spring Boot REST API!");
    }
}