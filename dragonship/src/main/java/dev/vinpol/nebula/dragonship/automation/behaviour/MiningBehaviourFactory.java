package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.GetSystemWaypoints200Response;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs.*;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence.sequence;
import static dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipLeafs.*;

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
    public Map<String, String> parameters() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("system", system.system());
        parameters.put("waypointType", waypointType.name());
        return parameters;
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

            return shipBehaviourFactoryCreator.treeOf(
                orbit(),
                sequence(
                    navigate(nearbyWaypointSymbol),
                    dock(),
                    refuel(),
                    orbit()
                ),
                extraction()
            );
        });
    }

    private Optional<Waypoint> findInSystem(SystemSymbol system, WaypointType waypointType) {
        GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(system.system(), 1, 1, waypointType);

        return response.getData()
            .stream()
            .findFirst();
    }
}
