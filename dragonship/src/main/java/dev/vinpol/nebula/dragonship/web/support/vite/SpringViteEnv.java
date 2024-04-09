package dev.vinpol.nebula.dragonship.web.support.vite;

import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ViteEnv;
import gg.jte.springframework.boot.autoconfigure.JteProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record SpringViteEnv(JteProperties properties, @Value("${vite.baseUrl}") String baseUrl) implements ViteEnv {
    @Override
    public boolean isProd() {
        return !properties.isDevelopmentMode();
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }
}
