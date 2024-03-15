package dev.vinpol.nebula.automation.algorithms.excavator;

import dev.vinpol.nebula.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.automation.behaviour.BehaviourFactoryRegistry;
import dev.vinpol.nebula.automation.behaviour.MiningBehaviourFactory;
import dev.vinpol.nebula.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNavRouteWaypoint;
import dev.vinpol.spacetraders.sdk.models.ShipRole;
import dev.vinpol.spacetraders.sdk.models.WaypointType;

import java.util.Objects;

public class ExcavatorAlgorithm implements ShipAlgorithm {

    private final BehaviourFactoryRegistry behaviourFactoryRegistry;

    public ExcavatorAlgorithm(BehaviourFactoryRegistry behaviourFactoryRegistry) {
        this.behaviourFactoryRegistry = behaviourFactoryRegistry;
    }

    @Override
    public ShipRole forRole() {
        return ShipRole.EXCAVATOR;
    }

    @Override
    public ShipBehaviour decideBehaviour(Ship ship) {
        Objects.requireNonNull(ship);

        ShipNavRouteWaypoint target = ship.getNav().getRoute().getDestination();
        MiningBehaviourFactory miningBehaviourFactory = behaviourFactoryRegistry.miningAutomation(SystemSymbol.tryParse(target.getSystemSymbol()), WaypointType.ENGINEERED_ASTEROID);

        return miningBehaviourFactory.create();
    }
}
