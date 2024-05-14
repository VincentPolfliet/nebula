package dev.vinpol.nebula.dragonship.automation.algorithms.excavator;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactoryCreator;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNavRouteWaypoint;
import dev.vinpol.spacetraders.sdk.models.ShipRole;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
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
            return shipBehaviourFactoryCreator.navigateToClosestMarket().create();
        }

        ShipNavRouteWaypoint target = ship.getNav().getRoute().getDestination();
        return shipBehaviourFactoryCreator.miningAutomation(SystemSymbol.tryParse(target.getSystemSymbol()), WaypointType.ENGINEERED_ASTEROID).create();
    }
}
