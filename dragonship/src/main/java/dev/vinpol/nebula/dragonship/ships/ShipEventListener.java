package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.automation.ShipCommander;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShipEventListener {

    private final ShipCommander shipCommander;
    private final ShipCache shipCache;

    public ShipEventListener(ShipCommander shipCommander, ShipCache shipCache) {
        this.shipCommander = shipCommander;
        this.shipCache = shipCache;
    }

    @EventListener
    public void onCommandShipEvent(CommandShipEvent event) {
        String shipSymbol = event.shipSymbol();

        Ship ship = shipCache.retrieve(shipSymbol);
        shipCommander.command(ship);
    }
}
