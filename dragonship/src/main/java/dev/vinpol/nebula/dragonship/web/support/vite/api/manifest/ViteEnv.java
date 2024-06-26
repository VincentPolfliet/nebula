package dev.vinpol.nebula.dragonship.web.support.vite.api.manifest;

public interface ViteEnv {
    boolean isProd();

    default boolean isDev() {
        return !isProd();
    }

    String getBaseUrl();
}
