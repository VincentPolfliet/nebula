package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.nebula.dragonship.geo.Coordinate;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.nebula.dragonship.web.Page;
import dev.vinpol.nebula.dragonship.web.utils.PagingUtils;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.System;
import dev.vinpol.spacetraders.sdk.models.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class FleetController {

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;
    private final TravelFuelAndTimerCalculator travelFuelAndTimerCalculator;

    public FleetController(FleetApi fleetApi, SystemsApi systemsApi, TravelFuelAndTimerCalculator travelFuelAndTimerCalculator) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.travelFuelAndTimerCalculator = travelFuelAndTimerCalculator;
    }

    @GetMapping(value = "/fleet")
    public String getFleet(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "limit", defaultValue = "10") int limit,
                           Model model) {

        Page contentPage = new Page();
        contentPage.setTitle("Fleet");

        model.addAttribute("page", contentPage);

        GetMyShips200Response response = fleetApi.getMyShips(page, limit);
        PagingUtils.setMetaOnModel(model, response.getMeta());
        model.addAttribute("ships", response.getData());

        return "fleet/index";
    }

    @PostMapping(value = "/fleet/{shipSymbol}/orbit")
    public String orbit(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        fleetApi.orbitShip(shipSymbol);

        Ship retrievedShip = fleetApi.getMyShip(shipSymbol).getData();
        model.addAttribute("ship", retrievedShip);
        return "fleet/ship-row";
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
        fleetApi.navigateShip(shipSymbol,
            new NavigateShipRequest()
                .waypointSymbol(targetSymbol)
        );

        model.addAttribute("ship", fleetApi.getMyShip(shipSymbol).getData());
        return "fleet/ship-row";
    }

    @GetMapping("/fleet/fuel/estimation")
    public String calculateFuel(@RequestParam("ship") String shipSymbol, @RequestParam("system") String systemSymbol, @RequestParam("origin") String originSymbol, @RequestParam("target") String targetSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();

        Waypoint originWaypoint = systemsApi.getWaypoint(systemSymbol, originSymbol).getData();
        Waypoint targetWaypoint = systemsApi.getWaypoint(systemSymbol, targetSymbol).getData();

        Map<ShipNavFlightMode, Long> cost = travelFuelAndTimerCalculator.calculateFuel(
            new Coordinate(originWaypoint.getX(), originWaypoint.getY()),
            new Coordinate(targetWaypoint.getX(), targetWaypoint.getY())
        );

        Map<ShipNavFlightMode, Double> duration = travelFuelAndTimerCalculator.calculateTime(
            new Coordinate(originWaypoint.getX(), originWaypoint.getY()),
            new Coordinate(targetWaypoint.getX(), targetWaypoint.getY()),
            ship.getEngine().getSpeed()
        );

        model.addAttribute("fuel", cost);
        model.addAttribute("duration", duration);

        return "fleet/fuel-estimation";
    }

    @PostMapping(value = "/fleet/{shipSymbol}/dock")
    public String dock(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        fleetApi.dockShip(shipSymbol);

        Ship retrievedShip = fleetApi.getMyShip(shipSymbol).getData();
        model.addAttribute("ship", retrievedShip);
        return "fleet/ship-row";
    }

    @PostMapping("/fleet/{shipSymbol}/flight")
    public String changeFlightMode(@PathVariable("shipSymbol") String shipSymbol, @RequestParam("mode") ShipNavFlightMode flightMode, Model model) {
        fleetApi.patchShipNav(shipSymbol, new PatchShipNavRequest().flightMode(flightMode));

        Ship updatedShip = fleetApi.getMyShip(shipSymbol).getData();
        model.addAttribute("ship", updatedShip);
        return "fleet/ship-row";
    }
}
