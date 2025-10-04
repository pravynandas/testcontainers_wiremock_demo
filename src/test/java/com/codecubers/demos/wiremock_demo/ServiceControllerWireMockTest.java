package com.codecubers.demos.wiremock_demo;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static com.codecubers.demos.wiremock_demo.helpers.ResourceHelper.getResourcePath;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ServiceControllerWireMockTest {

    @Container
    static WireMockContainer container = new WireMockContainer("wiremock/wiremock:2.35.0")
            .withCliArg("--global-response-templating");

    @BeforeAll
    public static void prepare() throws IOException, URISyntaxException, InterruptedException {
        assertThat(container).isNotNull();
        assertThat(container.isCreated());
        assertThat(container.isRunning());
        String serverUrl = container.getUrl("/");
        System.out.println("Server url:" + serverUrl);
        MyHttpClient.postMapping(serverUrl, getResourcePath("service-controller-hello-no-params.json"));
        MyHttpClient.postMapping(serverUrl, getResourcePath("service-controller-hello.json"));
        MyHttpClient.postMapping(serverUrl, getResourcePath("service-controller-users.json"));
    }

    @Test
    void testHelloUnknown() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/hello");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        AssertionsForClassTypes.assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        AssertionsForClassTypes.assertThat(response.body()).as("Wrong body").contains("Hello, Unknown!!");
    }

    @Test
    void testHelloTom() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/hello?name=Tom");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        AssertionsForClassTypes.assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        AssertionsForClassTypes.assertThat(response.body()).as("Wrong body").contains("Hello, Tom!!");
    }

    @Test
    void testHelloMike() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/hello?name=Mike");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        AssertionsForClassTypes.assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        AssertionsForClassTypes.assertThat(response.body()).as("Wrong body").contains("Hello, Mike!!");
    }


    @Test
    void testUsersTom123() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/users/Tom123");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        AssertionsForClassTypes.assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        AssertionsForClassTypes.assertThat(response.body()).as("Wrong body").contains("User ID: Tom123");
    }

    @Test
    void testUsersMike324() throws IOException, InterruptedException {
        String requestUrl = container.getUrl("/users/Mike324");
        final HttpResponse<String> response = MyHttpClient.getResponse(requestUrl);

        AssertionsForClassTypes.assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        AssertionsForClassTypes.assertThat(response.body()).as("Wrong body").contains("User ID: Mike324");
    }

}
