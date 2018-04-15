package com.github.tomciaaa.docker_hub_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomciaaa.docker_hub_api.model.AuthResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.net.URISyntaxException;

public class Auth {
    private static final String authBase = "https://auth.docker.io";
    public static AuthResponse GetAuthToken(String authService, String image) throws URISyntaxException, IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder uriBuilder = new URIBuilder(authBase);
            uriBuilder.setPath("/token");
            uriBuilder.setParameters(
                    new BasicNameValuePair("service", authService),
                    new BasicNameValuePair("scope", "repository:" + image + ":pull"));
            try (CloseableHttpResponse response = client.execute(new HttpGet(uriBuilder.build()))) {
                AuthResponse poo = new ObjectMapper().readValue(response.getEntity().getContent(), AuthResponse.class);
                return poo;
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public static AuthResponse GetAuthToken(String image) throws URISyntaxException, IOException {
        return GetAuthToken("registry.docker.io", image);
    }
}
