package com.github.tomciaaa.docker_hub_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ManifestResponseV1 implements ManifestResponse{
    private int schemaVersion;
    private List<ManifestResponseV1Blob> fsLayers;
    private String name;
    private String tag;
    private String architecture;
    private List<ManifestResponseV1History> history;
    private String rawContent;

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public List<ManifestResponseV1Blob> getFsLayers() {
        return fsLayers;
    }

    public void setFsLayers(List<ManifestResponseV1Blob> fsLayers) {
        this.fsLayers = fsLayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    @JsonIgnore
    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public List<ManifestResponseV1History> getHistory() {
        return history;
    }

    public void setHistory(List<ManifestResponseV1History> history) {
        this.history = history;
    }
}
