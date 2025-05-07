package dev.vinpol.nebula.dragonship.web.support.vite.api.manifest;

import dev.vinpol.nebula.dragonship.utils.StringUtils;

public record RemoteManifest(String host) implements Manifest {

    public RemoteManifest(String host) {
        if (host.endsWith("/")) {
            this.host = host.substring(0, host.length() - 1);
        } else {
            this.host = host;
        }
    }

    @Override
    public Chunk getChunk(String name) {
        Chunk chunk = new Chunk();
        chunk.setFile(host + StringUtils.appendIfNotStartsWith('/', name));
        chunk.setSrc(name);
        return chunk;
    }
}
