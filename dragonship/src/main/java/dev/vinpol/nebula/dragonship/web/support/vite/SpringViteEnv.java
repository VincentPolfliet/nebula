package dev.vinpol.nebula.dragonship.web.support.vite;

import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ViteEnv;
import gg.jte.springframework.boot.autoconfigure.JteProperties;
import org.springframework.stereotype.Component;

@Component
public record SpringViteEnv(JteProperties jteProperties, ViteSupportProperties viteSupportProperties) implements ViteEnv {
    @Override
    public boolean isProd() {
        return !jteProperties.isDevelopmentMode();
    }

    @Override
    public String getBaseUrl() {
        return viteSupportProperties.baseUrl();
    }
}
