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
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
//            .withMappingFromResource("HELLO", WiremockDemoApplicationTests.class, "hello-world.json")
//            .withMappingFromResource("WHATEVER", WiremockDemoApplicationTests.class, "whatever.json");
//            .withFileFromResource("hello", WiremockDemoApplicationTests.class, "hello-world.json");

    //client
    final static HttpClient client =  HttpClient.newHttpClient();

    @BeforeAll
    static void Prepare() throws IOException, InterruptedException, URISyntaxException {
        assert(container.isRunning());
        postMapping("hello-world.json");
        postMapping("whatever.json");
//        postMapping("scenario1.json");
//        postMapping("scenario2.json");
//        postMapping("scenario3.json");
    }


	@Test
	void testHello() throws IOException, InterruptedException {
        //response
        final HttpResponse<String> response = getResponse("/hello");
        //assert
        assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(200);
        assertThat(response.body()).as("Wrong body").contains("Hello, world!");
	}

    @Test
    void testWhatever() throws IOException, InterruptedException {
        //response
        final HttpResponse<String> response = getResponse("/whatever");
        //assert
        assertThat(response.statusCode()).as("Wrong Status Code").isEqualTo(202);
//        assertThat(response.body()).as("Wrong body").contains("Hello, world!");
    }

    private HttpResponse<String> getResponse(String path) throws IOException, InterruptedException {
        String url = container.getUrl(path);
        System.out.println("Url:" + url);

        //request
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        //response
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void postMapping(String resourceName) throws IOException, URISyntaxException, InterruptedException {
        postAdmin(resourceName, "mappings");
    }
    private static void postAdmin(String resourceName, String method) throws IOException, URISyntaxException, InterruptedException {
        //        final Path path = Paths.get("src","test","resources", "com","codecubers","demos","wiremock_demo", "hello-world.json").toAbsolutePath();
        final Path path = getResourcePath(resourceName);

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(container.getUrl("/__admin/" + method)))
                .POST(HttpRequest.BodyPublishers.ofFile(path))
                .build();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
        assertThat(response.statusCode())
                .isEqualTo(201);
    }

    private static Path getResourcePath(String resourceName) throws IOException, URISyntaxException {
        ClassLoader classLoader = WiremockDemoApplicationTests.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);

        if (resourceUrl == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceName);
        }

        URI resourceUri = resourceUrl.toURI();
        return Paths.get(resourceUri);
    }

}
