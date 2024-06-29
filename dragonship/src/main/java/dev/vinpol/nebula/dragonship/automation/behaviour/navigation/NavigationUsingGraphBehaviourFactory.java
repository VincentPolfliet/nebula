package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.automation.behaviour.RefuelShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviourFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.models.System;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.*;

public class NavigationUsingGraphBehaviourFactory implements ShipBehaviourFactory {

    private final Logger logger = LoggerFactory.getLogger(NavigationUsingGraphBehaviourFactory.class);

    private final FleetApi fleetApi;
    private final ShipEventNotifier shipEventNotifier;
    private final SystemsApi systemsApi;
    private final TravelCostCalculator travelCalculator;
    private final WaypointSymbol waypointSymbol;

    public NavigationUsingGraphBehaviourFactory(FleetApi fleetApi, ShipEventNotifier shipEventNotifier, SystemsApi systemsApi, TravelCostCalculator travelCalculator, WaypointSymbol waypointSymbol) {
        this.fleetApi = fleetApi;
        this.shipEventNotifier = shipEventNotifier;
        this.systemsApi = systemsApi;
        this.travelCalculator = travelCalculator;
        this.waypointSymbol = waypointSymbol;
    }

    @Override
    public ShipBehaviour create() {
        return ShipBehaviour.ofFunction(ship -> {
            var routeGraphCalculator = new RouteGraphCalculator(travelCalculator,
                new RouteGraphCalculator.Config(ship.getEngine().getSpeed())
            );

            if (ship.isFuelEmpty() && ship.getFuel().isNotInfinite()) {
                logger.trace("fuel is empty");
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.FUEL_IS_EMPTY));
            }

            if (ship.isNotInOrbit()) {
                logger.trace("ship is not in orbit");
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.NOT_IN_ORBIT));
            }

            String waypointSymbolString = waypointSymbol.waypoint();
            if (ship.isAtLocation(waypointSymbolString)) {
                logger.trace("ship is already at location");
                return ShipBehaviour.ofResult(ShipBehaviourResult.failure(FailureReason.ALREADY_AT_LOCATION));
            }

            WaypointSymbol currentLocationWaypointSymbol = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());

            System currentSystem = systemsApi.getSystem(currentLocationWaypointSymbol.system()).getData();
            Waypoint currentLocationWaypoint = systemsApi.getWaypoint(currentLocationWaypointSymbol.system(), currentLocationWaypointSymbol.waypoint()).getData();
            Waypoint targetWaypoint = systemsApi.getWaypoint(waypointSymbol.system(), waypointSymbolString).getData();

            List<Waypoint> marketWaypointsInSystem = getMarketWaypointsInSystem(currentSystem);

            Set<Waypoint> allWaypointsInGraph = new HashSet<>(marketWaypointsInSystem);
            allWaypointsInGraph.add(currentLocationWaypoint);
            allWaypointsInGraph.add(targetWaypoint);

            final Graph<String, TravelEdge> graph = routeGraphCalculator.getGraph(allWaypointsInGraph);

            return new ShipBehaviour() {

                private Route currentPath;
                private Waypoint currentLocationWaypoint;

                @Override
                public ShipBehaviourResult update(Ship ship) {
                    currentLocationWaypoint = systemsApi.getWaypoint(currentLocationWaypointSymbol.system(), currentLocationWaypointSymbol.waypoint()).getData();

                    if (Objects.equals(currentLocationWaypoint, targetWaypoint)) {
                        return ShipBehaviourResult.done();
                    }

                    if (currentPath == null) {
                        this.currentPath = tryToFindDirectPath(ship.getFuel(), currentLocationWaypoint, targetWaypoint, graph, allWaypointsInGraph);
                    }

                    logger.debug("currentPath: {}", currentPath);

                    if (currentPath == null || currentPath.isEmpty()) {
                        return ShipBehaviourResult.failure(FailureReason.NO_PATH_AVAILABLE);
                    }

                    for (RoutePart route : currentPath.routes()) {
                        Waypoint last = route.origin();
                        Waypoint next = route.target();

                        if (Objects.equals(currentLocationWaypoint, last)) {
                            boolean isPossibleRefuelingStation = currentLocationWaypoint.isMarket();

                            if (isPossibleRefuelingStation) {
                                // in case they don't sell fuel TODO check if they sell fuel
                                try {
                                    RefuelShip200Response response = fleetApi.refuelShip(ship.getSymbol(), RefuelShipBehaviour.getRefuelRequestForShip(ship));
                                    response.accept(ship);
                                } catch (Exception e) {
                                    // swallow exception
                                }
                            }

                            fleetApi.patchShipNav(ship.getSymbol(), new PatchShipNavRequest().flightMode(route.mode()));

                            logger.info("Sending '{}' to '{}' from '{}' (fuel: {}, time: {}s)", ship.getSymbol(), next.getSymbol(), currentLocationWaypoint.getSymbol(), route.fuelCost(), route.timeCost());

                            NavigateShip200ResponseData navigate = fleetApi.navigateShip(ship.getSymbol(), new NavigateShipRequest().waypointSymbol(next.getSymbol())).getData();
                            ship.nav(navigate.getNav());
                            ship.setFuel(navigate.getFuel());

                            if (ship.isFuelEmpty() || ship.considerFuelEmpty(0.2)) {
                                shipEventNotifier.setFuelIsAlmostEmpty(ship.getSymbol());
                            }

                            ShipNavRoute currentRoute = ship.getNav().getRoute();

                            OffsetDateTime arrival = currentRoute.getArrival();
                            shipEventNotifier.setNavigatingTo(ship.getSymbol(), waypointSymbol, currentRoute.getArrival());

                            return ShipBehaviourResult.waitUntil(arrival);
                        }
                    }

                    return ShipBehaviourResult.success();
                }
            };
        });
    }

    private List<Waypoint> getMarketWaypointsInSystem(System currentSystem) throws UncheckedIOException {
        return PageIterator.stream(PageIterator.INITIAL_PAGE, PageIterator.MAX_SIZE, req -> {
            GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentSystem.getSymbol(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
            return new Page<>(response.getData(), response.getMeta().getTotal());
        }).toList();
    }


    public static Route tryToFindDirectPath(ShipFuel fuel, Waypoint originWaypoint, Waypoint targetWaypoint, Graph<String, TravelEdge> graph, Set<Waypoint> waypoints) {
        ShipNavigator navigator = new ShipNavigator(graph, waypoints);
        return navigator.findPath(fuel, originWaypoint, targetWaypoint);
    }
}
