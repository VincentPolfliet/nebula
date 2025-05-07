package dev.vinpol.nebula.dragonship.web.support.vite;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vite")
public record ViteSupportProperties(
    String baseUrl,
    Integer devPort,
    String baseDirectory,
    String scriptName
) {
}
