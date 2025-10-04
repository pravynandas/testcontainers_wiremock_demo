package com.codecubers.demos.wiremock_demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MyHttpClient {

    final static HttpClient client = HttpClient.newHttpClient();

    static HttpRequest getRequest(String url) {
        return HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
    }

    static HttpRequest postRequest(String url, Path filePath) throws FileNotFoundException {
        return HttpRequest.newBuilder().uri(URI.create(WiremockDemoApplicationTests.container.getUrl(url))).POST(HttpRequest.BodyPublishers.ofFile(filePath)).build();
    }


    static void postAdmin(String method, Path filePath) throws IOException, URISyntaxException, InterruptedException {
        final HttpRequest request = postRequest("/__admin/" + method, filePath);

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(201);
    }

    static void postMapping(Path mappingsPath) throws IOException, URISyntaxException, InterruptedException {
        postAdmin("mappings", mappingsPath);
    }

    static HttpResponse<String> getResponse(String path) throws IOException, InterruptedException {
        final HttpRequest request = getRequest(WiremockDemoApplicationTests.container.getUrl(path));
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
