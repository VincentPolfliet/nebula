package dev.vinpol.nebula.dragonship.web.galaxy;

import dev.vinpol.nebula.dragonship.shared.storage.CacheStorage;
import org.springframework.stereotype.Component;

@Component
public class MapDataCache extends CacheStorage<String, MapData> {
    public MapDataCache() {
        super(String.class, MapData.class);
    }
}
