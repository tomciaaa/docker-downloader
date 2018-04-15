package com.github.tomciaaa.docker_hub_api;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest {
    @Test
    public void gets_busybox_pull_token() throws IOException, URISyntaxException {
        assertThat(Auth.GetAuthToken("registry.docker.io", "_/busybox").getAccessToken()).isNotBlank();
    }
}
