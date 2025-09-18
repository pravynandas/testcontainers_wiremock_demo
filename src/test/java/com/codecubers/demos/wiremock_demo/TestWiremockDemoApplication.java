package com.codecubers.demos.wiremock_demo;

import org.springframework.boot.SpringApplication;

public class TestWiremockDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(WiremockDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
