package com.github.tomciaaa.docker_hub_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomciaaa.docker_hub_api.model.ManifestResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;

public class Manifest {
    /*
    manifestJson="$(
		curl -fsSL \
			-H "Authorization: Bearer $token" \
			-H 'Accept: application/vnd.docker.distribution.manifest.v2+json' \
			-H 'Accept: application/vnd.docker.distribution.manifest.list.v2+json' \
			-H 'Accept: application/vnd.docker.distribution.manifest.v1+json' \
			"$registryBase/v2/$image/manifests/$digest"
			*/
    private static final String registryBase = "https://registry-1.docker.io/v2/";
    public static ManifestResponse Fetch(String imageAndTag, String authToken) throws URISyntaxException, IOException {
        String[] parts = imageAndTag.split(":", 2);
        if (parts.length < 2 ) {
            throw new RuntimeException("Invalidiamge");
        }
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder builder = new URIBuilder(registryBase);
            builder.setPath("/v2/"+parts[0]+"/manifests/"+parts[1]);
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.addHeader("Authorization",  "Bearer "+authToken) ;
            httpGet.addHeader("Accept", "application/vnd.docker.distribution.manifest.v2+json");
            try (CloseableHttpResponse execute = client.execute(httpGet)) {
                String s = EntityUtils.toString(execute.getEntity());
                ManifestResponse obj = new ObjectMapper().readValue(s, ManifestResponse.class);
                obj.setRawContent(s);
                return obj;
            }
        } catch (IOException e) {
            throw e;
        }
    }
}
