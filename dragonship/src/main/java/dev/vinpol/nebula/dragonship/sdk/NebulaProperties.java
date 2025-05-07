package dev.vinpol.nebula.dragonship.sdk;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nebula.st")
public record NebulaProperties(
        @Value("${nebula.st.token:}") @Nullable String token,
        @Value("${nebula.st.auto-register}") boolean autoRegisterIfTokenIsEmpty,
        @Value("${nebula.st.auto-register.symbol:}") @Nullable String autoRegisterSymbol,
        @Value("${nebula.st.url}") String url) {
}
