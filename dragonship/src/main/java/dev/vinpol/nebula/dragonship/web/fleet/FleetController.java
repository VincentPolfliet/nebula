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
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.nebula.dragonship.utils.time.TimeWizard;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
import dev.vinpol.nebula.dragonship.web.utils.PagingUtils;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.System;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import org.jgrapht.Graph;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.*;

@Controller
public class FleetController {

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;
    private final ShipCommander shipCommander;
    private final AutomationFactory shipBehaviourFactoryCreator;
    private final ShipAlgorithmResolver shipAlgorithmResolver;
    private final Clock clock;

    public FleetController(FleetApi fleetApi, SystemsApi systemsApi, ShipCommander shipCommander, AutomationFactory shipBehaviourFactoryCreator, ShipAlgorithmResolver shipAlgorithmResolver, Clock clock) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.shipCommander = shipCommander;
        this.shipBehaviourFactoryCreator = shipBehaviourFactoryCreator;
        this.shipAlgorithmResolver = shipAlgorithmResolver;
        this.clock = clock;
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

        HtmlPage contentPage = new HtmlPage();
        contentPage.setTitle("Fleet");

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
        return "fleet/index";
    }

    @PostMapping(value = "/fleet/{shipSymbol}/orbit")
    public String orbit(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        fleetApi.orbitShip(shipSymbol);

        Ship retrievedShip = fleetApi.getMyShip(shipSymbol).getData();
        setShipStateOnModel(model, retrievedShip);
        return "fleet/ship-row";
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
        model.addAttribute("system", system);

        return "fleet/navigate-modal";
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


        List<Waypoint> possibleWaypoints = PageIterator.stream(PageIterator.INITIAL_PAGE, PageIterator.MAX_SIZE, req -> {
            GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(currentLocationSymbol.system(), req.page(), req.size(), null, WaypointTraitSymbol.MARKETPLACE);
            return new Page<>(response.getData(), response.getMeta().getTotal());
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

    @PostMapping(value = "/fleet/{shipSymbol}/dock")
    public void dock(@PathVariable("shipSymbol") String shipSymbol) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();

        shipCommander.command(ship, ShipBehaviourLeafs.dock());
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
}
