package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.nebula.automation.ShipCommander;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShipEventListener {

    private final ShipCommander shipCommander;
    private final ShipStorage shipStorage;

    public ShipEventListener(ShipCommander shipCommander, ShipStorage shipStorage) {
        this.shipCommander = shipCommander;
        this.shipStorage = shipStorage;
    }

    @EventListener
    public void onCommandShipEvent(CommandShipEvent event) {
        String shipSymbol = event.shipSymbol();

        Ship ship = shipStorage.retrieve(shipSymbol);
        shipCommander.command(ship);
    }
}
