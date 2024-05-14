package dev.vinpol.nebula.dragonship.web.config.support.vite;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vite")
public record ViteProperties(
        @Value("${vite.baseUrl}") String baseUrl) {
}
