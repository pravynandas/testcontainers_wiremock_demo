package com.codecubers.demos.wiremock_demo;

import com.codecubers.demos.wiremock_demo.services.HelloService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceController.class)
class ServiceControllerWebMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HelloService service;

    @Test
    public void hello() throws Exception {
        //Mock service responses
        when(service.sayHello("")).thenReturn("Hello, Missing!!");
        when(service.sayHello("Unknown")).thenReturn("Hello, Unknown!!");
        when(service.sayHello("Mock")).thenReturn("Hello, Mock!!");

        this.mockMvc.perform(get("/hello")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Hello, Unknown!!")));
        this.mockMvc.perform(get("/hello?name=Mock")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Hello, Mock!!")));
    }

}