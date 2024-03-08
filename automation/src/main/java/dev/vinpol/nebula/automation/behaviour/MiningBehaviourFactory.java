package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.nebula.automation.tree.AutomationLeaf;
import dev.vinpol.nebula.automation.tree.AutomationTorterra;
import dev.vinpol.nebula.automation.tree.ShipTreeBehaviour;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.vinpol.nebula.automation.tree.ShipLeafs.*;
import static dev.vinpol.torterra.Torterra.*;
import static java.util.function.Predicate.not;

public class MiningBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(MiningBehaviourFactory.class);

    private final SystemsApi systemsApi;
    private final AutomationTorterra torterra;
    private final SystemSymbol system;
    private final WaypointType waypointType;

    public MiningBehaviourFactory(SystemsApi systemsApi, BehaviourFactoryRegistry behaviourFactoryRegistry, SystemSymbol system, WaypointType waypointType) {
        this.systemsApi = systemsApi;
        this.torterra = new AutomationTorterra(behaviourFactoryRegistry);
        this.system = system;
        this.waypointType = waypointType;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction(ship -> {
            Objects.requireNonNull(ship);

            // find the nearby engineered asteroid
            Optional<Waypoint> nearbyAsteroidOptional = findInSystem(system, waypointType);

            if (nearbyAsteroidOptional.isEmpty()) {
                logger.info("Found no nearby asteroid in '{}'", system.system());
                return ShipBehaviour.finished();
            }

            Waypoint nearbyWaypoint = nearbyAsteroidOptional.get();
            WaypointSymbol nearbyWaypointSymbol = WaypointSymbol.tryParse(nearbyWaypoint.getSymbol());

            return new ShipTreeBehaviour(plant(
                cargoIsNotFull(),
                hasNoActiveCooldown(),
                hasFuelLeft(),
                isNotInTransit(),
                safeSequence(
                    predicate(Ship::isDocked),
                    orbit()
                ),
                safeSequence(
                    predicate(inShip -> !isAtLocation(inShip, nearbyWaypoint)),
                    navigateToWaypoint(nearbyWaypointSymbol),
                    dock(),
                    refuel(),
                    orbit()
                ),
                extraction()
            ));
        });
    }

    private Optional<Waypoint> findInSystem(SystemSymbol system, WaypointType waypointType) {
        GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(system.system(), 1, 10, waypointType, new String[0]);

        List<Waypoint> data = response.getData();
        return data
            .stream()
            .findFirst();
    }

    private AutomationLeaf extraction() {
        return torterra.extraction();
    }

    private AutomationLeaf navigateToWaypoint(WaypointSymbol waypoint) {
        return torterra.navigate(waypoint);
    }

    private AutomationLeaf orbit() {
        return torterra.orbit();
    }

    private AutomationLeaf dock() {
        return torterra.dock();
    }

    private AutomationLeaf refuel() {
        return torterra.refuel();
    }

    private boolean isAtLocation(Ship ship, Waypoint waypoint) {
        ShipNav nav = ship.getNav();
        ShipNavRoute route = nav.getRoute();
        ShipNavRouteWaypoint destination = route.getDestination();

        if (destination == null) {
            return false;
        }

        return waypoint.getSymbol().equals(route.getDestination().getSymbol());
    }
}
