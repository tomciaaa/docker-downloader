package com.github.tomciaaa.docker_hub_api;

import com.github.tomciaaa.docker_hub_api.model.ManifestResponse;
import com.github.tomciaaa.docker_hub_api.model.ManifestResponseV2;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class ManifestTest {
    @Test
    public void gets_busybox_manifest() throws IOException, URISyntaxException {
        ManifestResponse manifest = Manifest.Fetch(
                "library/busybox:latest",
                Auth.GetAuthToken("library/busybox").getAccessToken());
    }
}
