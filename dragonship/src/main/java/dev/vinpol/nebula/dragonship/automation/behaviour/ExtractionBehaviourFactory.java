package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.ExtractResources201ResponseData;
import dev.vinpol.spacetraders.sdk.models.ExtractResourcesRequest;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;

public class ExtractionBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(ExtractionBehaviourFactory.class);

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
            public ShipBehaviorResult update(Ship ship) {
                if (ship.isCargoFull()) {
                    return ShipBehaviorResult.failure(FailureReason.CARGO_IS_FULL);
                }

                if (ship.isNotInOrbit()) {
                    return ShipBehaviorResult.failure(FailureReason.NOT_IN_ORBIT);
                }

                if (ship.hasActiveCooldown()) {
                    return ShipBehaviorResult.failure(FailureReason.ACTIVE_COOLDOWN);
                }

                ExtractResources201ResponseData extractionResponse = fleetApi.extractResources(ship.getSymbol(), new ExtractResourcesRequest()).getData();

                ship.setCargo(extractionResponse.getCargo());

                if (ship.isCargoFull()) {
                    shipEventNotifier.setCargoFull(ship.getSymbol());
                }

                ship.setCooldown(extractionResponse.getCooldown());

                // cooldown is always active after mining
                OffsetDateTime expiration = ship.getCooldown().getExpiration();
                shipEventNotifier.setWaitUntilCooldown(ship.getSymbol(), expiration);

                logger.info("Ship COOLDOWN until '{}' after EXTRACTION", expiration);
                return ShipBehaviorResult.waitUntil(expiration);
            }
        };
    }
}
