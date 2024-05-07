package dev.vinpol.nebula.dragonship.sdk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nebula.st")
public record NebulaProperties(
    @Value("${nebula.st.token}") String token,
    @Value("${nebula.st.url}") String url) {
}
