package com.github.tomciaaa.docker_hub_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomciaaa.docker_hub_api.model.ManifestResponse;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class Blob {
    public static void Fetch(String registryBase, String image, String blobId, String authToken, OutputStream outputStream) throws URISyntaxException, IOException {
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder builder = new URIBuilder(registryBase);
            builder.setPath("/v2/"+image+"/blobs/"+blobId);
            HttpGet httpGet = new HttpGet(builder.build());
            if (authToken != null) {
                httpGet.addHeader("Authorization",  "Bearer "+authToken) ;
            }
            httpGet.addHeader("Accept", "application/vnd.docker.image.rootfs.diff.tar.gzip");
            try (CloseableHttpResponse execute = client.execute(httpGet)) {
                execute.getEntity().writeTo(outputStream);
            }
        } catch (IOException e) {
            throw e;
        }

    }

    public static void Fetch(String image, String blobId, String authToken, OutputStream outputStream) throws URISyntaxException, IOException {
        Fetch("https://registry-1.docker.io/v2/", image, blobId, authToken, outputStream);
    }
}
