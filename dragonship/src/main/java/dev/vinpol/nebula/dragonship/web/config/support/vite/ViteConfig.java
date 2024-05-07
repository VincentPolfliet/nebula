package dev.vinpol.nebula.dragonship.web.config.support.vite;

import dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest.ManifestLoader;
import dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest.ManifestLoaderImpl;
import dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest.ManifestParser;
import dev.vinpol.nebula.dragonship.web.config.support.vite.api.manifest.ViteEnv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ViteConfig {

    @Bean
    public ManifestLoader manifestLoader(ViteEnv viteEnv, ManifestParser manifestParser) {
        return new ManifestLoaderImpl(viteEnv, manifestParser, null);
    }
}
