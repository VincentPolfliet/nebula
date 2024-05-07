package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShipSynchronizeTask implements Runnable {

    private final ShipCache shipCache;
    private final FleetApi fleetApi;

    public ShipSynchronizeTask(ShipCache shipCache, FleetApi fleetApi) {
        this.shipCache = shipCache;
        this.fleetApi = fleetApi;
    }

    @EventListener(classes = SyncEvent.class)
    void onSync() {
        run();
    }

    @Override
    public void run() {
        for (Ship ship : fleetApi.getMyShips()) {
            shipCache.store(ship.getSymbol(), ship);
        }
    }
}
