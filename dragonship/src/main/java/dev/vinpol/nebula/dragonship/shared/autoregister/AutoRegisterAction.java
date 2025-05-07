package dev.vinpol.nebula.dragonship.shared.autoregister;

import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.api.DefaultApi;
import dev.vinpol.spacetraders.sdk.models.Register201ResponseData;
import dev.vinpol.spacetraders.sdk.models.RegisterRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AutoRegisterAction {

    private final DefaultApi defaults;

    public AutoRegisterAction(ApiClient client) {
        this.defaults = client.defaults();
    }

    public RegisteredAgent register(String symbol) {
        Objects.requireNonNull(symbol);

        if (symbol.isEmpty() || symbol.isBlank()) {
            throw new IllegalStateException("Symbol can't be blank or empty");
        }

        Register201ResponseData response = defaults.register(
            new RegisterRequest()
                .symbol(symbol)
        ).getData();

        return new RegisteredAgent(response.getAgent().getSymbol(), response.getToken());
    }
}
