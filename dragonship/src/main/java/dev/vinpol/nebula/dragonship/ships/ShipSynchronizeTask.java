package dev.vinpol.nebula.dragonship.ships;

import dev.failsafe.RateLimiter;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShips200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ShipSynchronizeTask implements Runnable {

    private final ShipStorage shipStorage;
    private final FleetApi fleetApi;
    private final RateLimiter<Object> rateLimiter;

    public ShipSynchronizeTask(ShipStorage shipStorage, FleetApi fleetApi) {
        this.shipStorage = shipStorage;
        this.fleetApi = fleetApi;
        this.rateLimiter = RateLimiter.burstyBuilder(1, Duration.ofSeconds(5)).build();
    }

    @EventListener(classes = SyncEvent.class)
    void onSync() {
        run();
    }

    @Override
    public void run() {
        if (!rateLimiter.tryAcquirePermit()) {
            return;
        }

        GetMyShips200Response myShips200Response = fleetApi.getMyShips(1, 10);

        for (Ship ship : myShips200Response.getData()) {
            shipStorage.store(ship.getSymbol(), ship);
        }
    }
}
