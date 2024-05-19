package dev.vinpol.nebula.dragonship.shared.i18n;

import com.fasterxml.jackson.databind.JsonNode;
import dev.vinpol.nebula.dragonship.shared.storage.CacheStorage;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessagesCache extends CacheStorage<Locale, JsonNode> {
    protected MessagesCache() {
        super(Locale.class, JsonNode.class);
    }
}
