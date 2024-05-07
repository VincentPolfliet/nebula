package dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ManifestLoaderImpl implements ManifestLoader {

    private final ViteEnv env;
    private final ManifestParser parser;
    private final Path manifestPath;

    public ManifestLoaderImpl(ViteEnv env, ManifestParser parser, Path manifestPath) {
        this.env = env;
        this.parser = parser;
        this.manifestPath = manifestPath;
    }

    @Override
    public Manifest load() {
        if (env.isDev()) {
            return new DevManifest(env.getBaseUrl());
        }

        if (manifestPath == null) {
            throw new IllegalArgumentException("manifestPath is null");
        }

        try (var reader = Files.newBufferedReader(manifestPath)) {
            return parser.parse(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
