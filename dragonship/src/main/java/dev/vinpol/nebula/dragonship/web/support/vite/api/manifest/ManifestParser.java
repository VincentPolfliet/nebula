package dev.vinpol.nebula.dragonship.web.support.vite.api.manifest;

import java.io.Reader;

public interface ManifestParser {
    Manifest parse(Reader reader);
}
