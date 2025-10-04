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
        return HttpRequest.newBuilder().uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofFile(filePath)).build();
    }


    static void postAdmin(String url, String method, Path filePath) throws IOException, URISyntaxException, InterruptedException {
        final HttpRequest request = postRequest(url + "__admin/" + method, filePath);

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(201);
    }

    static void postMapping(String url, Path mappingsPath) throws IOException, URISyntaxException, InterruptedException {
        postAdmin(url, "mappings", mappingsPath);
    }

    static HttpResponse<String> getResponse(String url) throws IOException, InterruptedException {
        final HttpRequest request = getRequest(url);
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
