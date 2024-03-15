package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.ExtractResourcesRequest;
import dev.vinpol.spacetraders.sdk.models.Ship;

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
        return new ShipBehaviour() {

            @Override
            public String getName() {
                return "extraction";
            }

            @Override
            public ShipBehaviourResult update(Ship ship) {
                if (ship.isCargoFull()) {
                    return ShipBehaviourResult.failure(FailureReason.CARGO_IS_FULL);
                }

                if (!ship.isInOrbit()) {
                    return ShipBehaviourResult.failure(FailureReason.NOT_IN_ORBIT);
                }

                if (ship.hasActiveCooldown()) {
                    return ShipBehaviourResult.failure(FailureReason.ACTIVE_COOLDOWN);
                }

                var extractionResponse = fleetApi.extractResources(ship.getSymbol(), new ExtractResourcesRequest()).getData();

                ship.setCargo(extractionResponse.getCargo());

                if (ship.isCargoFull()) {
                    shipEventNotifier.setCargoFull(ship.getSymbol());
                }

                ship.setCooldown(extractionResponse.getCooldown());

                // cooldown is always active after mining
                OffsetDateTime expiration = ship.getCooldown().getExpiration();
                shipEventNotifier.setWaitUntilCooldown(ship.getSymbol(), expiration);
                return ShipBehaviourResult.waitUntil(expiration);
            }
        };
    }
}
