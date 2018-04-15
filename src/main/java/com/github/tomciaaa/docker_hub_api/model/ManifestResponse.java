package com.github.tomciaaa.docker_hub_api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties({"rawContent"})
public class ManifestResponse {
    private String schemaVersion;
    private String mediaType;
    private ManifestResponseConfig config;
    private List<ManifestResponseConfig> layers;
    private String rawContent;

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public ManifestResponseConfig getConfig() {
        return config;
    }

    public void setConfig(ManifestResponseConfig config) {
        this.config = config;
    }

    public List<ManifestResponseConfig> getLayers() {
        return layers;
    }

    public void setLayers(List<ManifestResponseConfig> layers) {
        this.layers = layers;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }
}
