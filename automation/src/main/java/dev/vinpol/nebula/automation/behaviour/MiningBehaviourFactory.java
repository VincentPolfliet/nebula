package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.nebula.automation.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.GetSystemWaypoints200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import dev.vinpol.torterra.Leaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.vinpol.nebula.automation.behaviour.tree.ShipBehaviourLeafs.*;
import static dev.vinpol.nebula.automation.behaviour.tree.ShipLeafs.*;
import static dev.vinpol.torterra.Torterra.predicate;
import static dev.vinpol.torterra.Torterra.safeSequence;

public class MiningBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(MiningBehaviourFactory.class);

    private final SystemsApi systemsApi;
    private final ShipBehaviourFactoryCreator shipBehaviourFactoryCreator;
    private final SystemSymbol system;
    private final WaypointType waypointType;

    public MiningBehaviourFactory(SystemsApi systemsApi, ShipBehaviourFactoryCreator shipBehaviourFactoryCreator, SystemSymbol system, WaypointType waypointType) {
        this.systemsApi = systemsApi;
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.system = system;
        this.waypointType = waypointType;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction(ship -> {
            Objects.requireNonNull(ship);

            Optional<Waypoint> nearbyAsteroidOptional = findInSystem(system, waypointType);

            if (nearbyAsteroidOptional.isEmpty()) {
                logger.info("Found no nearby asteroid in '{}'", system.system());
                return ShipBehaviour.finished();
            }

            Waypoint nearbyWaypoint = nearbyAsteroidOptional.get();
            WaypointSymbol nearbyWaypointSymbol = WaypointSymbol.tryParse(nearbyWaypoint.getSymbol());

            List<Leaf<Ship>> sequence = List.of(
                cargoIsNotFull(),
                hasNoActiveCooldown(),
                hasFuelLeft(),
                isNotInTransit(),
                safeSequence(
                    isDocked(),
                    orbit()
                ),
                safeSequence(
                    predicate(inShip -> !isAtLocation(inShip, nearbyWaypoint)),
                    navigate(nearbyWaypointSymbol),
                    dock(),
                    refuel(),
                    orbit()
                ),
                extraction()
            );

            return shipBehaviourFactoryCreator.sequenceOf(sequence);
        });
    }

    private Optional<Waypoint> findInSystem(SystemSymbol system, WaypointType waypointType) {
        GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(system.system(), 1, 10, waypointType, new String[0]);

        List<Waypoint> data = response.getData();
        return data
            .stream()
            .findFirst();
    }

    private boolean isAtLocation(Ship ship, Waypoint waypoint) {
        return ship.isAtLocation(waypoint.getSymbol());
    }

}
