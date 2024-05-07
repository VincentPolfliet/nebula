package dev.vinpol.nebula.dragonship.web.config.support.vite.jte;

import dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest.Manifest;
import dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest.ManifestChunkRenderer;
import gg.jte.Content;
import gg.jte.TemplateOutput;

import java.io.IOException;

public class JteManifestChunkContent implements Content {

    private final Manifest manifest;
    private final String asset;
    private final ManifestChunkRenderer manifestChunkRenderer;

    JteManifestChunkContent(Manifest manifest, String asset) {
        this.manifest = manifest;
        this.asset = asset;

        this.manifestChunkRenderer = new ManifestChunkRenderer();
    }

    @Override
    public void writeTo(TemplateOutput output) {
        try {
            String renderedChunk = manifestChunkRenderer.render(manifest, asset);
            output.writeContent(renderedChunk);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
