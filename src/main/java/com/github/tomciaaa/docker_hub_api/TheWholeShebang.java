package com.github.tomciaaa.docker_hub_api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.tomciaaa.docker_hub_api.model.AuthResponse;
import com.github.tomciaaa.docker_hub_api.model.ManifestResponse;
import com.github.tomciaaa.docker_hub_api.model.ManifestResponseConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TheWholeShebang {

    private static void stickContentIntoTarStream(TarArchiveOutputStream tarOutput, String name, byte[] content) throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry(name);
        entry.setSize(content.length);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(content);
        tarOutput.closeArchiveEntry();
    }

    public static void FetchImage(String imageName, String tag, OutputStream output) throws IOException, URISyntaxException {
        AuthResponse auth = Auth.GetAuthToken(imageName);
        ManifestResponse fetch = Manifest.Fetch(imageName + ":" + tag, auth.getToken());
        TarArchiveOutputStream tarOutput = new TarArchiveOutputStream(output);

        ByteArrayOutputStream configBytes = new ByteArrayOutputStream(fetch.getConfig().getSize());
        Blob.Fetch(imageName, fetch.getConfig().getDigest(), auth.getToken(), configBytes);

        //Stip sha256: prefix and give it .json extension
        String configFn = fetch.getConfig().getDigest().substring(7)+".json";
        stickContentIntoTarStream(tarOutput, configFn, configBytes.toByteArray());

        String parent = "";
        List<String> layerFiles = new ArrayList<>();
        for (int i=0; i<fetch.getLayers().size(); i++) {
            ManifestResponseConfig layer = fetch.getLayers().get(i);
            String layerId = DigestUtils.sha256Hex(parent + "\n" + layer.getDigest()+"\n");

            if (i+1 < fetch.getLayers().size()){
                writeDummyJson(tarOutput, layerId, parent);
            } else {
                writeProperJson(tarOutput, layerId, parent, configBytes);
            }

            WriteThing(tarOutput, layerId);

            // Fetch the blob
            // TODO: non-UTC crap
            if (new Date().getTime() - auth.issuedAt.getTime() > auth.getExpiresIn() * 1000 - 10000) {
                auth = Auth.GetAuthToken(imageName);
            }
            layerFiles.add(layerId+"/layer.tar");
            TarArchiveEntry entry = new TarArchiveEntry(layerId+"/layer.tar");
            entry.setSize(layer.getSize());
            tarOutput.putArchiveEntry(entry);
            Blob.Fetch(imageName, layer.getDigest(), auth.getToken(), tarOutput);
            tarOutput.closeArchiveEntry();

            // Set parent to this one
            parent = layerId;
        }

        ArrayNode arrNode = (ArrayNode) new ObjectMapper().readTree("[]");
        ObjectNode node = arrNode.addObject();
        node.put("Config", configFn);
        node.putArray("RepoTags").add(imageName.replaceAll("^library/", "")+":"+tag);
        ArrayNode layersNode = node.putArray("Layers");
        layerFiles.forEach(layersNode::add);
        stickContentIntoTarStream(tarOutput, "manifest.json", new ObjectMapper().writeValueAsBytes(arrNode));


        node = (ObjectNode) new ObjectMapper().readTree("{}");
        node.putObject(imageName.replaceAll("^library/", "")).put(tag, parent);
        stickContentIntoTarStream(tarOutput, "repositories", new ObjectMapper().writeValueAsBytes(node));

        tarOutput.finish();
    }

    private static void writeProperJson(TarArchiveOutputStream tarOutput, String layerId, String parent, ByteArrayOutputStream configBytes) throws IOException {
        ObjectNode placeHolder = (ObjectNode) new ObjectMapper().readTree(dummyJsonBytes(layerId, parent));
        JsonNode jsonNode = new ObjectMapper().readTree(configBytes.toByteArray());
        jsonNode.fields().forEachRemaining(x -> {
            if (!(x.getKey().equalsIgnoreCase("history") || x.getKey().equalsIgnoreCase("rootfs")))
                placeHolder.put(x.getKey(), x.getValue());
        });
        byte[] content = new ObjectMapper().writeValueAsBytes(placeHolder);
        stickContentIntoTarStream(tarOutput, layerId+"/json", content);
    }

    private static void WriteThing(TarArchiveOutputStream tarOutput, String layerId) throws IOException {
        byte[] content = "1.0\n".getBytes();
        stickContentIntoTarStream(tarOutput, layerId + "/VERSION", content);
    }

    private static void writeDummyJson(TarArchiveOutputStream tarOutput, String id, String parent) throws IOException {
        byte[] content = dummyJsonBytes(id, parent);
        stickContentIntoTarStream(tarOutput, id+"/json", content);
    }

    private static byte[] dummyJsonBytes(String id, String parent) {
        StringBuilder b = new StringBuilder();
        b.append("{\n");
        b.append("        \"id\": \""+id+"\",");
        if (parent != null && !parent.isEmpty()) {
            b.append("        \"parent\": \"" + parent + "\",");
        }
        b.append(
                "        \"created\": \"0001-01-01T00:00:00Z\",\n" +
                "        \"container_config\": {\n" +
                "                \"Hostname\": \"\",\n" +
                "                \"Domainname\": \"\",\n" +
                "                \"User\": \"\",\n" +
                "                \"AttachStdin\": false,\n" +
                "                \"AttachStdout\": false,\n" +
                "                \"AttachStderr\": false,\n" +
                "                \"Tty\": false,\n" +
                "                \"OpenStdin\": false,\n" +
                "                \"StdinOnce\": false,\n" +
                "                \"Env\": null,\n" +
                "                \"Cmd\": null,\n" +
                "                \"Image\": \"\",\n" +
                "                \"Volumes\": null,\n" +
                "                \"WorkingDir\": \"\",\n" +
                "                \"Entrypoint\": null,\n" +
                "                \"OnBuild\": null,\n" +
                "                \"Labels\": null\n" +
                "        }\n" +
                "}"
        );
        return b.toString().getBytes();
    }
}
