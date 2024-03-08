package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.shared.storage.CacheStorage;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.stereotype.Component;

@Component
public class ShipStorage extends CacheStorage<String, Ship> {

    public ShipStorage() {
        super(String.class, Ship.class);
    }
}
