package dev.vinpol.nebula.dragonship.web.support.vite.api.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    @JsonProperty("file")
    private String file;

    @JsonProperty("src")
    private String src;

    @JsonProperty("isEntry")
    private boolean isEntry;

    @JsonProperty("isDynamicEntry")
    private boolean isDynamicEntry;

    @JsonProperty("css")
    private List<String> css = new ArrayList<>();

    @JsonProperty("assets")
    private List<String> assets = new ArrayList<>();

    @JsonProperty("imports")
    private List<String> imports = new ArrayList<>();

    @JsonProperty("dynamicImports")
    private List<String> dynamicImports = new ArrayList<>();

    public Chunk() {
    }

    public String getFile() {
        return this.file;
    }

    public String getSrc() {
        return this.src;
    }

    public boolean isEntry() {
        return this.isEntry;
    }

    public boolean isDynamicEntry() {
        return this.isDynamicEntry;
    }

    public List<String> getCss() {
        return this.css;
    }

    public List<String> getAssets() {
        return this.assets;
    }

    public List<String> getImports() {
        return this.imports;
    }

    public List<String> getDynamicImports() {
        return this.dynamicImports;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public void setEntry(boolean isEntry) {
        this.isEntry = isEntry;
    }

    public void setDynamicEntry(boolean isDynamicEntry) {
        this.isDynamicEntry = isDynamicEntry;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }

    public void setAssets(List<String> assets) {
        this.assets = assets;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public void setDynamicImports(List<String> dynamicImports) {
        this.dynamicImports = dynamicImports;
    }
}
