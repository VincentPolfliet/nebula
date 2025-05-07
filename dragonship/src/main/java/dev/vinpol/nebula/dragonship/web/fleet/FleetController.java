package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.Route;
import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.RouteGraphCalculator;
import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.ShipNavigator;
import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.TravelEdge;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourLeafs;
import dev.vinpol.nebula.dragonship.automation.command.ShipCommander;
import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.nebula.dragonship.utils.time.TimeWizard;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
import dev.vinpol.nebula.dragonship.web.galaxy.TravelCostEstimate;
import dev.vinpol.nebula.dragonship.web.utils.PagingUtils;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.System;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class FleetController {

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;
    private final ShipCommander shipCommander;
    private final AutomationFactory shipBehaviourFactoryCreator;
    private final ShipAlgorithmResolver shipAlgorithmResolver;
    private final Clock clock;
    private final TravelCostCalculator travelCostCalculator;

    public FleetController(FleetApi fleetApi, SystemsApi systemsApi, ShipCommander shipCommander, AutomationFactory shipBehaviourFactoryCreator, ShipAlgorithmResolver shipAlgorithmResolver, Clock clock, TravelCostCalculator travelCostCalculator) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.shipCommander = shipCommander;
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.shipAlgorithmResolver = shipAlgorithmResolver;
        this.clock = clock;
        this.travelCostCalculator = travelCostCalculator;
    }

    public static Optional<TravelCostEstimate> findCostByMode(List<TravelCostEstimate> costs, ShipNavFlightMode flightMode) {
        return costs.stream().filter(cost -> cost.mode() == flightMode).findFirst();
    }


    @ModelAttribute("clock")
    public TimeWizard clock() {
        return new TimeWizard(clock);
    }

    @ModelAttribute("automationSupport")
    public Set<ShipRole> automationSupport() {
        return shipAlgorithmResolver.getSupported();
    }

    @GetMapping(value = "/fleet")
    public String getFleet(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "10") int limit,
                           Model model) {

        HtmlPage contentPage = new HtmlPage("Fleet");

        model.addAttribute("page", contentPage);

        GetMyShips200Response response = fleetApi.getMyShips(page, limit);
        PagingUtils.setMetaOnModel(model, response.getMeta());

        List<Ship> ships = response.getData();
        model.addAttribute("ships", ships);

        Map<Ship, ShipBehaviour> shipBehaviourMap = new HashMap<>();

        for (Ship ship : ships) {
            shipBehaviourMap.put(ship, shipCommander.getCurrentBehaviour(ship).orElse(null));
        }

        model.addAttribute("behaviours", shipBehaviourMap);
        return "fleet/fleet";
    }

    private void setShipStateOnModel(Model model, Ship retrievedShip) {
        model.addAttribute("ship", retrievedShip);
        model.addAttribute("currentBehaviour", shipCommander.getCurrentBehaviour(retrievedShip).orElse(null));
    }

    @GetMapping("/fleet/{shipSymbol}/navigate")
    public String navigate(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();
        model.addAttribute("ship", ship);

        GetSystem200Response systemResponse = systemsApi.getSystem(ship.getNav().getSystemSymbol());

        System system = systemResponse.getData();
        model.addAttribute("waypoints",
            system.getWaypoints()
                .stream()
                .map((sw) -> new SystemWaypointWithDistance(sw, travelCostCalculator.calculateDistance(GridXY.toCoordinate(sw), GridXY.toCoordinate(ship.getNav().getRoute().getOrigin()))))
                .sorted(Comparator.comparing(SystemWaypointWithDistance::distance))
                .toList()
        );

        return "fleet/navigate-modal";
    }

    @GetMapping("/fleet/{ship}/navigate/estimate")
    public String navigateDetail(@PathVariable("ship") String shipSymbol, @RequestParam(value = "target", required = false) String waypointSymbol, Model model) throws InterruptedException {
        if (waypointSymbol != null && !waypointSymbol.isEmpty()) {
            Ship ship = fleetApi.getMyShip(shipSymbol).getData();
            Waypoint waypoint = systemsApi.getWaypoint(ship.getNav().getSystemSymbol(), waypointSymbol).getData();

            NavigationCostCalculator costCalculator = new NavigationCostCalculator(ship, waypoint, travelCostCalculator);
            costCalculator.populateModel(model);
        } else {
            model.addAttribute("costs", Collections.emptyList());
        }

        return "fleet/navigate-table";
    }

    @PostMapping("/fleet/{shipSymbol}/navigate")
    public String navigate(@PathVariable("shipSymbol") String shipSymbol, @RequestParam("target") String targetSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();

        shipCommander.command(ship, shipBehaviourFactoryCreator.treeOf(ShipBehaviourLeafs.navigate(WaypointSymbol.tryParse(targetSymbol))));

        setShipStateOnModel(model, ship);
        return "fleet/ship-row";
    }

    @GetMapping(value = "/fleet/{shipSymbol}/navigate/simulate.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RouteSimulation> simulate(@PathVariable("shipSymbol") String shipSymbol, @RequestParam("target") String targetSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();

        WaypointSymbol currentLocationSymbol = WaypointSymbol.tryParse(ship.getNav().getWaypointSymbol());
        Waypoint currentWaypoint = systemsApi.getWaypoint(currentLocationSymbol.system(), currentLocationSymbol.waypoint()).getData();

        WaypointSymbol targetLocationSymbol = WaypointSymbol.tryParse(targetSymbol);
        Waypoint targetWaypoint = systemsApi.getWaypoint(targetLocationSymbol.system(), targetLocationSymbol.waypoint()).getData();

        List<Waypoint> possibleWaypoints = PageIterator.stream(req -> {
            GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentLocationSymbol.system(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
            return new Page<>(response.getData(), response.getMeta().total());
        }).toList();

        RouteGraphCalculator calculator = new RouteGraphCalculator(new TravelCostCalculator(), new RouteGraphCalculator.Config(ship.getEngine().getSpeed()));

        Set<Waypoint> all = new HashSet<>();
        all.add(currentWaypoint);
        all.addAll(possibleWaypoints);
        all.add(targetWaypoint);

        Graph<String, TravelEdge> graph = calculator.getGraph(all);
        ShipNavigator navigator = new ShipNavigator(graph, all);
        Route route = navigator.findPath(ship.getFuel(), currentWaypoint, targetWaypoint);

        return ResponseEntity.ok(new RouteSimulation(
            currentLocationSymbol.waypoint(),
            targetLocationSymbol.waypoint(),
            possibleWaypoints.stream().map(Waypoint::getSymbol).toList(),
            route
        ));
    }

    @PostMapping(value = "/fleet/{shipSymbol}/orbit")
    public String orbit(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        fleetApi.orbitShip(shipSymbol);

        Ship retrievedShip = fleetApi.getMyShip(shipSymbol).getData();
        setShipStateOnModel(model, retrievedShip);
        return "fleet/ship-row";
    }

    @PostMapping(value = "/fleet/{shipSymbol}/dock")
    public String dock(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        fleetApi.dockShip(shipSymbol);

        Ship retrievedShip = fleetApi.getMyShip(shipSymbol).getData();
        setShipStateOnModel(model, retrievedShip);
        return "fleet/ship-row";
    }

    @PostMapping("/fleet/{shipSymbol}/flight")
    public String changeFlightMode(@PathVariable("shipSymbol") String shipSymbol, @RequestParam("mode") ShipNavFlightMode flightMode, Model model) {
        fleetApi.patchShipNav(shipSymbol, new PatchShipNavRequest().flightMode(flightMode));

        Ship updatedShip = fleetApi.getMyShip(shipSymbol).getData();
        setShipStateOnModel(model, updatedShip);
        return "fleet/ship-row";
    }

    @PostMapping("/fleet/{shipSymbol}/automation/start")
    public String startAutomation(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();
        shipCommander.commandWithFunction(ship, new KeepRunningUntilFailure(shipAlgorithmResolver));

        Ship updatedShip = fleetApi.getMyShip(shipSymbol).getData();
        setShipStateOnModel(model, updatedShip);
        return "fleet/ship-row";
    }

    @PostMapping("/fleet/{shipSymbol}/automation/stop")
    public String stopAutomation(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();
        shipCommander.cancel(ship);

        Ship updatedShip = fleetApi.getMyShip(shipSymbol).getData();
        setShipStateOnModel(model, updatedShip);
        return "fleet/ship-row";
    }


    static class NavigationCostCalculator {
        private final Ship ship;
        private final TravelCostCalculator travelCostCalculator;
        private final Map<ShipNavFlightMode, Long> fuelCosts;
        private final Map<ShipNavFlightMode, Duration> travelDurations;

        public NavigationCostCalculator(Ship ship, Waypoint targetWaypoint, TravelCostCalculator calculator) {
            this.ship = ship;
            this.travelCostCalculator = calculator;

            GridXY origin = GridXY.toCoordinate(ship.getNav().getRoute().getOrigin());
            GridXY destination = GridXY.toCoordinate(targetWaypoint);

            this.fuelCosts = calculator.calculateFuel(origin, destination);
            this.travelDurations = calculator.calculateTime(origin, destination, ship.getEngine().getSpeed());
        }

        public void populateModel(Model model) {
            ShipNavFlightMode suggested = calculateSuggestedFlightMode();
            Map<ShipNavFlightMode, Long> remainingFuel = calculateRemainingFuel();

            List<TravelCostEstimate> estimates = Arrays.stream(ShipNavFlightMode.values())
                .map(mapToTravelCostEstimate(suggested, remainingFuel))
                .sorted(getTravelCostEstimateComparator(suggested))
                .toList();

            model.addAttribute("costs", estimates);
            model.addAttribute("keys", estimates.stream().map(TravelCostEstimate::mode).toList());
        }

        private ShipNavFlightMode calculateSuggestedFlightMode() {
            ShipNavFlightMode bestByFuel = travelCostCalculator.selectBestFlightModeByFuel(fuelCosts);
            ShipNavFlightMode bestByDuration = travelCostCalculator.selectBestFlightModeByDuration(travelDurations);
            return bestByFuel.equals(bestByDuration) ? bestByFuel : bestByDuration;
        }

        private Map<ShipNavFlightMode, Long> calculateRemainingFuel() {
            Map<ShipNavFlightMode, Long> remainingFuel = new EnumMap<>(ShipNavFlightMode.class);
            ShipFuel shipFuel = ship.getFuel();

            for (ShipNavFlightMode mode : ShipNavFlightMode.values()) {
                long remaining = shipFuel.isNotInfinite() ? shipFuel.getCurrent() - fuelCosts.get(mode) : 0;
                remainingFuel.put(mode, remaining);
            }

            return remainingFuel;
        }

        private Map<ShipNavFlightMode, TravelCostEstimate> calculateTravelCostEstimates(ShipNavFlightMode suggested) {
            Map<ShipNavFlightMode, Long> remainingFuel = calculateRemainingFuel();

            List<TravelCostEstimate> estimates = Arrays.stream(ShipNavFlightMode.values())
                .map(mapToTravelCostEstimate(suggested, remainingFuel))
                .sorted(getTravelCostEstimateComparator(suggested))
                .toList();

            return estimates
                .stream()
                .collect(Collectors.toMap(TravelCostEstimate::mode, Function.identity(), (a, b) -> a, LinkedHashMap::new));
        }

        private Function<ShipNavFlightMode, TravelCostEstimate> mapToTravelCostEstimate(ShipNavFlightMode suggested, Map<ShipNavFlightMode, Long> remainingFuel) {
            return mode -> new TravelCostEstimate(
                mode,
                fuelCosts.get(mode),
                travelDurations.get(mode),
                remainingFuel.get(mode),
                mode == suggested
            );
        }

        private static Comparator<TravelCostEstimate> getTravelCostEstimateComparator(ShipNavFlightMode suggested) {
            return (o1, o2) -> {
                if (o1.mode() == suggested) {
                    return -1;
                } else if (o2.mode() == suggested) {
                    return 1;
                } else if (o1.mode() == ShipNavFlightMode.DRIFT || o2.mode() == ShipNavFlightMode.DRIFT) {
                    return o1.mode() == ShipNavFlightMode.DRIFT ? 1 : -1;
                } else {
                    int durationComparison = o1.duration().compareTo(o2.duration());
                    if (durationComparison != 0) {
                        return durationComparison;
                    }
                    return Long.compare(o1.fuelCost(), o2.fuelCost());
                }
            };
        }
    }
}
