package com.codecubers.demos.wiremock_demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Testcontainers
class WiremockDemoApplicationTests {

    @Container
    final static WireMockContainer container = new WireMockContainer("wiremock/wiremock:2.35.0");

    static Path getResourcePath(String resourceName) throws IOException, URISyntaxException {
        ClassLoader classLoader = WiremockDemoApplicationTests.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);

        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        URI resourceUri = resourceUrl.toURI();
        return Paths.get(resourceUri);
    }

    @BeforeAll
    static void Prepare() throws IOException, InterruptedException, URISyntaxException {
        assert (container.isRunning());
        MyHttpClient.postMapping(getResourcePath("hello-world.json"));
        MyHttpClient.postMapping(getResourcePath("whatever.json"));
    }


    @Test
    void testHello() throws IOException, InterruptedException {
        final HttpResponse<String> response = MyHttpClient.getResponse("/hello");

        assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        assertThat(response.body()).as("Wrong body").contains("Hello, world!");
    }

    @Test
    void testWhatever() throws IOException, InterruptedException {
        final HttpResponse<String> response = MyHttpClient.getResponse("/whatever");

        assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(202);
    }


}
