package dev.vinpol.nebula.dragonship.web.support.vite.api.manifest;

import j2html.rendering.HtmlBuilder;

import java.io.IOException;
import java.util.Objects;

import static j2html.TagCreator.link;
import static j2html.TagCreator.script;

public class ManifestChunkRenderer {
    private static final RenderOptions DEFAULT_OPTIONS = new RenderOptions(false);

    public String render(Manifest manifest, String assetName) throws IOException {
        return render(manifest, assetName, DEFAULT_OPTIONS);
    }

    public String render(Manifest manifest, String assetName, RenderOptions renderOptions) throws IOException {
        StringHtmlBuilder builder = StringHtmlBuilder.pretty();
        render(manifest, assetName, renderOptions, builder);
        return builder.toString();
    }

    public void render(Manifest manifest, String asset, RenderOptions renderOptions, HtmlBuilder<?> builder) throws IOException {
        Objects.requireNonNull(manifest);
        Objects.requireNonNull(asset);
        Objects.requireNonNull(renderOptions);
        Objects.requireNonNull(builder);

        Chunk chunk = manifest.getChunk(asset);
        renderChunk(chunk, manifest.getImportedChunks(asset), renderOptions, builder);
    }

    private static void renderChunk(Chunk chunk, Iterable<Chunk> importedChunks, RenderOptions renderOptions, HtmlBuilder<?> builder) throws IOException {
        /*
        <!-- for cssFile of manifest[name].css -->
        <link rel="stylesheet" href="/{{ cssFile }}" />
        */
        renderCss(chunk, builder);

        /*
        <!-- for chunk of importedChunks(manifest, name) -->
        <!-- for cssFile of chunk.css -->
        <link rel="stylesheet" href="/{{ cssFile }}" />
         */
        for (Chunk importedChunk : importedChunks) {
            renderCss(importedChunk, builder);
        }

        // <script type="module" src="/{{ manifest[name].file }}"></script>
        script().withType("module").withSrc(chunk.getFile()).render(builder);

        for (Chunk importedChunk : importedChunks) {
            // <!-- for chunk of importedChunks(manifest, name) -->
            // <link rel="modulepreload" src="/{{ chunk.file }}" />

            if (renderOptions.includeModulePreload()) {
                link()
                    .withRel("modulepreload")
                    .attr("src",  importedChunk.getFile())
                    .render(builder);
            }
        }
    }

    private static void renderCss(Chunk chunk, HtmlBuilder<?> result) throws IOException {
        for (String css : chunk.getCss()) {
            link()
                .withRel("stylesheet")
                .withHref(css)
                .render(result);
        }
    }

    public record RenderOptions(boolean includeModulePreload) {

    }
}
