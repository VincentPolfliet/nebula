package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.ExtractResources201ResponseData;
import dev.vinpol.spacetraders.sdk.models.ExtractResourcesRequest;

import java.time.OffsetDateTime;

public class ExtractionBehaviourFactory implements ShipBehaviourFactory {

    private final FleetApi fleetApi;
    private final ShipEventNotifier shipEventNotifier;

    public ExtractionBehaviourFactory(FleetApi fleetApi, ShipEventNotifier shipEventNotifier) {
        this.fleetApi = fleetApi;
        this.shipEventNotifier = shipEventNotifier;
    }

    @Override
    public ShipBehaviour create() {
        return (ship) -> {
            ExtractResources201ResponseData extractionResponse
                = fleetApi.extractResources(ship.getSymbol(), new ExtractResourcesRequest()).getData();

            ship.setCargo(extractionResponse.getCargo());

            if (ship.isCargoFull()) {
                shipEventNotifier.setCargoFull(ship.getSymbol());
            }

            ship.setCooldown(extractionResponse.getCooldown());

            // cooldown is always active after mining
            OffsetDateTime expiration = ship.getCooldown().getExpiration();
            shipEventNotifier.setWaitUntilCooldown(ship.getSymbol(), expiration);
            return ShipBehaviourResult.waitUntil(expiration);
        };
    }
}
