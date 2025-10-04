package com.codecubers.demos.wiremock_demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static com.codecubers.demos.wiremock_demo.helpers.ResourceHelper.getResourcePath;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@Testcontainers
class WiremockDemoApplicationTests {

    @Container
    final static WireMockContainer container = new WireMockContainer("wiremock/wiremock:2.35.0");

    @BeforeAll
    static void Prepare() throws IOException, InterruptedException, URISyntaxException {
        assert (container.isRunning());
        String serverUrl = container.getUrl("/");
        MyHttpClient.postMapping(serverUrl, getResourcePath("hello-world.json"));
        MyHttpClient.postMapping(serverUrl, getResourcePath("whatever.json"));
    }


    @Test
    void testHello() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/hello");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        assertThat(response.body()).as("Wrong body").contains("Hello, world!");
    }

    @Test
    void testWhatever() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/whatever");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(202);
    }


}
