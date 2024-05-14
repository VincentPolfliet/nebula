package dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Manifest {
    Chunk getChunk(String name);

    default Iterable<Chunk> getImportedChunks(String name) {
        return new Iterable<>() {
            @NotNull
            @Override
            public Iterator<Chunk> iterator() {
                return getChunk(name)
                        .getImports()
                        .stream()
                        .map(inName -> getChunk(inName))
                        .iterator();
            }
        };
    }
}
