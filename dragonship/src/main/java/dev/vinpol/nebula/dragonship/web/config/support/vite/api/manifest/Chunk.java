package dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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
}
