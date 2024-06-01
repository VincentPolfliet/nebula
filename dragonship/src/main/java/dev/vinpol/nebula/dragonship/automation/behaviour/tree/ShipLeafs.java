package dev.vinpol.nebula.dragonship.automation.behaviour.tree;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

import static java.util.function.Predicate.not;

public class ShipLeafs {
    private static final Logger logger = LoggerFactory.getLogger(ShipLeafs.class);

    private ShipLeafs() {

    }

    private static ShipBehaviour predicateBehaviour(String description, Predicate<Ship> predicate) {
        return new ShipBehaviour() {

            private final Predicate<Ship> innerPredicate = predicate(description, predicate);

            @Override
            public String getName() {
                return description;
            }

            @Override
            public ShipBehaviourResult update(Ship ship) {
                return innerPredicate.test(ship) ? ShipBehaviourResult.success() : ShipBehaviourResult.failure("check '%s' returned false".formatted(description));
            }
        };
    }

    private static Predicate<Ship> predicate(String description, Predicate<Ship> predicate) {
        return new DescriptivePredicate<>(description, predicate);
    }

    public static Predicate<Ship> cargoIsNotFull() {
        return predicate("Ship has place in cargo", not(ship -> {
            logger.debug("Checking if Ship has place in cargo");
            return ship.isCargoFull();
        }));
    }

    public static Predicate<Ship> hasNoActiveCooldown() {
        return predicate("Ship has no cooldown", not(Ship::hasActiveCooldown));
    }

    public static ShipBehaviour hasFuelLeft() {
        return predicateBehaviour("Ship has fuel left", ship -> !ship.isFuelEmpty());
    }

    public static Predicate<Ship> isNotInTransit() {
        return predicate("Ship is not in transit", not(Ship::isInTransit));
    }

    public static ShipBehaviour isDocked() {
        return predicateBehaviour("Ship is docked", ship -> {
            logger.debug("ship docked: {}", ship.isDocked());
            return ship.isDocked();
        });
    }

    public static Predicate<Ship> isAtLocation(Waypoint waypoint) {
        return predicate("Ship is at location", ship -> {
            logger.debug("ship is at location: {}", ship.isAtLocation(waypoint.getSymbol()));
            return ship.isAtLocation(waypoint.getSymbol());
        });
    }

    public static ShipBehaviour isNotAtLocation(WaypointSymbol waypoint) {
        return predicateBehaviour("Ship is NOT at location", ship -> {
            logger.debug("ship is NOT at location: {}", !ship.isAtLocation(waypoint.waypoint()));
            return !ship.isAtLocation(waypoint.waypoint());
        });
    }

    public static ShipBehaviour isNotInOrbit() {
        return predicateBehaviour("Ship is NOT in orbit", ship -> {
            logger.debug("ship NOT in orbit: {}", !ship.isInOrbit());
            return !ship.isInOrbit();
        });
    }

    public static ShipBehaviour fuelIsEmpty() {
        return predicateBehaviour("Fuel is EMPTY", Ship::isFuelEmpty);
    }

    public static ShipBehaviour isNotDocked() {
        return predicateBehaviour("ship is NOT docked", ship -> !ship.isDocked());
    }

    public static ShipBehaviour fuelIsNotFull() {
        return predicateBehaviour("ship is NOT FULL", not(Ship::isFuelFull));
    }
}
