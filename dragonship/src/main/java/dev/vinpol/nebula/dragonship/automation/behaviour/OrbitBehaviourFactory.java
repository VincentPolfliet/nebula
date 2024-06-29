package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.OrbitShip200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OrbitBehaviourFactory implements ShipBehaviourFactory {


    private final Logger logger = LoggerFactory.getLogger(OrbitBehaviourFactory.class);

    private final FleetApi fleetApi;
    private final ShipEventNotifier eventNotifier;

    public OrbitBehaviourFactory(FleetApi fleetApi, ShipEventNotifier eventNotifier) {
        this.fleetApi = fleetApi;
        this.eventNotifier = eventNotifier;
    }

    @Override
    public ShipBehaviour create() {
        return new ShipBehaviour() {
            @Override
            public String getName() {
                return "orbit";
            }

            @Override
            public ShipBehaviourResult update(Ship ship) {
                logger.info("OrbitBehaviour is running");

                // orbit can always be called, even when ship is not docked
                OrbitShip200Response orbit = fleetApi.orbitShip(ship.getSymbol());
                ship.setNav(orbit.getData().getNav());
                eventNotifier.setOrbited(ship.getSymbol());

                return ShipBehaviourResult.done();
            }
        };
    }
}
