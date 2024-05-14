package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.dragonship.shared.synchronize.SynchronizeTask;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShipSynchronizeTask implements SynchronizeTask<Ship> {

    private final FleetApi fleetApi;

    public ShipSynchronizeTask(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @Override
    @EventListener(classes = SyncEvent.class)
    public void run() {
        // this will be fucking slow for many ships FIXME
        for (Ship ship : fleetApi.getMyShips()) {
            fleetApi.getMyShip(ship.getSymbol());
        }
    }

    @Override
    public String name() {
        return "SHIP-SYNC-TASK";
    }

    @Override
    public Class<Ship> typeClazz() {
        return Ship.class;
    }
}
