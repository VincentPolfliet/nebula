package dev.vinpol.nebula.dragonship.automation.command;

import dev.vinpol.nebula.dragonship.automation.command.events.CommandShipEvent;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ShipCommandEventListener {

    private final ShipCommander shipCommander;
    private final FleetApi fleetApi;

    public ShipCommandEventListener(ShipCommander shipCommander, FleetApi fleetApi) {
        this.shipCommander = shipCommander;
        this.fleetApi = fleetApi;
    }


    @EventListener
    public void onCommandShipEvent(CommandShipEvent event) {
        String shipSymbol = event.shipSymbol();

        Ship ship = fleetApi.getMyShip(shipSymbol).getData();
        shipCommander.command(ship);
    }
}
