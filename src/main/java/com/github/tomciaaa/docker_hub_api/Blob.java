package com.github.tomciaaa.docker_hub_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomciaaa.docker_hub_api.model.ManifestResponse;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Blob {
    private static final String registryBase = "https://registry-1.docker.io/v2/";

    public static void Fetch(String image, String blobId, String authToken, OutputStream outputStream) throws URISyntaxException, IOException {
        try(DefaultHttpClient client = new DefaultHttpClient()) {
            client.setRedirectStrategy(new DefaultRedirectStrategy(){
                @Override
                public HttpUriRequest getRedirect( HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                    URI uri = getLocationURI(request, response, context);
                    switch (request.getRequestLine().getMethod()) {
                        case "GET":
                            return new HttpGet(uri) {
                                @Override
                                public Header[] getHeaders(String name) {
                                    return Arrays.stream(super.getHeaders(name)).filter(x -> !x.getName().equalsIgnoreCase("authorization")).collect(Collectors.toList()).toArray(new Header[0]);
                                }
                            };
                        case "HEAD":
                            return new HttpHead(uri) {
                                @Override
                                public Header[] getHeaders(String name) {
                                    return Arrays.stream(super.getHeaders(name)).filter(x -> !x.getName().equalsIgnoreCase("authorization")).collect(Collectors.toList()).toArray(new Header[0]);
                                }
                            };
                        default:
                            throw new RuntimeException("Poo");
                    }
                }
            });
            URIBuilder builder = new URIBuilder(registryBase);
            builder.setPath("/v2/"+image+"/blobs/"+blobId);
            HttpGet httpGet = new HttpGet(builder.build());
            httpGet.addHeader("Authorization",  "Bearer "+authToken) ;
            httpGet.addHeader("Accept", "application/vnd.docker.image.rootfs.diff.tar.gzip");
            try (CloseableHttpResponse execute = client.execute(httpGet)) {
                execute.getEntity().writeTo(outputStream);
            }
        } catch (IOException e) {
            throw e;
        }

    }
}
