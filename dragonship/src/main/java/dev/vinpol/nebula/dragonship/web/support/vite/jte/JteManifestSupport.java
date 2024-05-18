package dev.vinpol.nebula.dragonship.web.support.vite.jte;

import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ManifestLoader;
import gg.jte.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class JteManifestSupport {
    private static ManifestLoader manifestSupplier;

    JteManifestSupport() {
    }

    @Autowired
    void setManifestSupplier(ManifestLoader manifestSupplier) {
        if (JteManifestSupport.manifestSupplier != null) {
            throw new IllegalStateException("manifestSupplier is already set");
        }

        JteManifestSupport.manifestSupplier = manifestSupplier;
    }

    public static Content asset(String assetName) {
        Objects.requireNonNull(assetName);

        return new JteManifestChunkContent(manifestSupplier.load(), assetName);
    }
}
