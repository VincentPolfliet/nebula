package dev.vinpol.nebula.automation.algorithms.excavator;

import dev.vinpol.nebula.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.automation.behaviour.MiningBehaviourFactory;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNavRouteWaypoint;
import dev.vinpol.spacetraders.sdk.models.ShipRole;
import dev.vinpol.spacetraders.sdk.models.WaypointType;

import java.util.Objects;

public class ExcavatorAlgorithm implements ShipAlgorithm {

    private final ShipBehaviourFactoryCreator shipBehaviourFactoryCreator;

    public ExcavatorAlgorithm(ShipBehaviourFactoryCreator shipBehaviourFactoryCreator) {
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
    }

    @Override
    public ShipRole forRole() {
        return ShipRole.EXCAVATOR;
    }

    @Override
    public ShipBehaviour decideBehaviour(Ship ship) {
        Objects.requireNonNull(ship);

        if (ship.hasActiveCooldown()) {
            return shipBehaviourFactoryCreator.cooldownActive(ship.getCooldown().getExpiration());
        }

        if (ship.isInTransit()) {
            return shipBehaviourFactoryCreator.inTransit(ship.getNav().getRoute().getArrival());
        }

        if (ship.isCargoFull()) {
            // TODO: lookup closest market and sell of our cargo we no longer needed
            return shipBehaviourFactoryCreator.navigateToClosestMarket();
        }

        ShipNavRouteWaypoint target = ship.getNav().getRoute().getDestination();
        return shipBehaviourFactoryCreator.miningAutomation(SystemSymbol.tryParse(target.getSystemSymbol()), WaypointType.ENGINEERED_ASTEROID);
    }
}
