package com.github.tomciaaa.docker_hub_api;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class Blob {
    public static void Fetch(String registryBase, String image, String blobId, String authToken, OutputStream outputStream, String tarEntryName) throws URISyntaxException, IOException {
        TarArchiveOutputStream tarStream = tarEntryName != null? (TarArchiveOutputStream) outputStream : null;
        try(CloseableHttpClient client = HttpClients.createDefault()) {
            URIBuilder builder = new URIBuilder(registryBase);
            builder.setPath("/v2/"+image+"/blobs/"+blobId);
            HttpGet httpGet = new HttpGet(builder.build());
            if (authToken != null) {
                httpGet.addHeader("Authorization",  "Bearer "+authToken) ;
            }
            httpGet.addHeader("Accept", "application/vnd.docker.image.rootfs.diff.tar.gzip");
            try (CloseableHttpResponse execute = client.execute(httpGet)) {
                if (tarEntryName != null) {
                    TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(tarEntryName);
                    tarArchiveEntry.setSize(execute.getEntity().getContentLength());
                    tarStream.putArchiveEntry(tarArchiveEntry);
                    execute.getEntity().writeTo(tarStream);
                    tarStream.closeArchiveEntry();
                } else {
                    execute.getEntity().writeTo(outputStream);
                }
            }
        } catch (IOException e) {
            throw e;
        }

    }

    public static void Fetch(String image, String blobId, String authToken, OutputStream outputStream) throws URISyntaxException, IOException {
        Fetch("https://registry-1.docker.io/v2/", image, blobId, authToken, outputStream, null);
    }
}
