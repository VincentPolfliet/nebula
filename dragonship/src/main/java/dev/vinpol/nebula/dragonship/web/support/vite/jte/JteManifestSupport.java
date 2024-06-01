package dev.vinpol.nebula.dragonship.web.support.vite.jte;

import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ManifestLoader;
import gg.jte.Content;
import gg.jte.springframework.boot.autoconfigure.JteProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public final class JteManifestSupport {
    private static ManifestLoader manifestSupplier;

    JteManifestSupport() {
    }

    @Autowired
    void setManifestSupplier(ManifestLoader manifestSupplier) {
        JteManifestSupport.manifestSupplier = manifestSupplier;
    }

    public static Content asset(String assetName) {
        Objects.requireNonNull(assetName);

        return new JteManifestChunkContent(manifestSupplier.load(), assetName);
    }
}
