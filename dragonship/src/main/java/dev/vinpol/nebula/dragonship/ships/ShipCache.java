package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.shared.storage.CacheStorage;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.stereotype.Component;

@Component
public class ShipCache extends CacheStorage<String, Ship> {

    public ShipCache() {
        super(String.class, Ship.class);
    }
}
