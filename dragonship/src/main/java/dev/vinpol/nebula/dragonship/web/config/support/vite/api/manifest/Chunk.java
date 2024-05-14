package dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest;

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

    @JsonProperty("file")
    public void setFile(String file) {
        this.file = file;
    }

    @JsonProperty("src")
    public void setSrc(String src) {
        this.src = src;
    }

    @JsonProperty("isEntry")
    public void setEntry(boolean isEntry) {
        this.isEntry = isEntry;
    }

    @JsonProperty("isDynamicEntry")
    public void setDynamicEntry(boolean isDynamicEntry) {
        this.isDynamicEntry = isDynamicEntry;
    }

    @JsonProperty("css")
    public void setCss(List<String> css) {
        this.css = css;
    }

    @JsonProperty("assets")
    public void setAssets(List<String> assets) {
        this.assets = assets;
    }

    @JsonProperty("imports")
    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    @JsonProperty("dynamicImports")
    public void setDynamicImports(List<String> dynamicImports) {
        this.dynamicImports = dynamicImports;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Chunk)) return false;
        final Chunk other = (Chunk) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$file = this.getFile();
        final Object other$file = other.getFile();
        if (this$file == null ? other$file != null : !this$file.equals(other$file)) return false;
        final Object this$src = this.getSrc();
        final Object other$src = other.getSrc();
        if (this$src == null ? other$src != null : !this$src.equals(other$src)) return false;
        if (this.isEntry() != other.isEntry()) return false;
        if (this.isDynamicEntry() != other.isDynamicEntry()) return false;
        final Object this$css = this.getCss();
        final Object other$css = other.getCss();
        if (this$css == null ? other$css != null : !this$css.equals(other$css)) return false;
        final Object this$assets = this.getAssets();
        final Object other$assets = other.getAssets();
        if (this$assets == null ? other$assets != null : !this$assets.equals(other$assets)) return false;
        final Object this$imports = this.getImports();
        final Object other$imports = other.getImports();
        if (this$imports == null ? other$imports != null : !this$imports.equals(other$imports)) return false;
        final Object this$dynamicImports = this.getDynamicImports();
        final Object other$dynamicImports = other.getDynamicImports();
        if (this$dynamicImports == null ? other$dynamicImports != null : !this$dynamicImports.equals(other$dynamicImports))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Chunk;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $file = this.getFile();
        result = result * PRIME + ($file == null ? 43 : $file.hashCode());
        final Object $src = this.getSrc();
        result = result * PRIME + ($src == null ? 43 : $src.hashCode());
        result = result * PRIME + (this.isEntry() ? 79 : 97);
        result = result * PRIME + (this.isDynamicEntry() ? 79 : 97);
        final Object $css = this.getCss();
        result = result * PRIME + ($css == null ? 43 : $css.hashCode());
        final Object $assets = this.getAssets();
        result = result * PRIME + ($assets == null ? 43 : $assets.hashCode());
        final Object $imports = this.getImports();
        result = result * PRIME + ($imports == null ? 43 : $imports.hashCode());
        final Object $dynamicImports = this.getDynamicImports();
        result = result * PRIME + ($dynamicImports == null ? 43 : $dynamicImports.hashCode());
        return result;
    }

    public String toString() {
        return "Chunk(file=" + this.getFile() + ", src=" + this.getSrc() + ", isEntry=" + this.isEntry() + ", isDynamicEntry=" + this.isDynamicEntry() + ", css=" + this.getCss() + ", assets=" + this.getAssets() + ", imports=" + this.getImports() + ", dynamicImports=" + this.getDynamicImports() + ")";
    }
}
