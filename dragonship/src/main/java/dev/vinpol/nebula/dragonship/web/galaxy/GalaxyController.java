package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.web.utils.PagingUtils;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.models.System;
import one.util.streamex.StreamEx;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class GalaxyController {

    private static final UUID SEED = UUID.randomUUID();

    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    private final ObjectMapper objectMapper;

    public GalaxyController(FleetApi fleetApi, SystemsApi systemsApi, ObjectMapper objectMapper) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/galaxy")
    public String galaxy() {
        return "redirect:/galaxy/systems";
    }

    @GetMapping("/galaxy/systems")
    public String getGalaxySystems(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                   Model model) {
        GetSystems200Response systems = systemsApi.getSystems(page, pageSize);
        PagingUtils.setMetaOnModel(model, systems.getMeta());
        model.addAttribute("systems", systems.getData());
        return "galaxy/systems";
    }

    @GetMapping("/galaxy/system/{systemSymbol}")
    public String system(@PathVariable("systemSymbol") String symbol) {
        SystemSymbol systemSymbol = SystemSymbol.tryParse(symbol);
        return "redirect:/galaxy/system/%s/map".formatted(systemSymbol.system());
    }

    @GetMapping("/galaxy/system/{system}/markets.json")
    @ResponseBody
    public ResponseEntity<Object> getMarketsOfSystem(@PathVariable("system") String symbol) {
        return ResponseEntity.ok(systemsApi.getSystemWaypoints(symbol, 1, 10, null, WaypointTraitSymbol.MARKETPLACE));
    }

    @GetMapping("/galaxy/waypoint/{waypointSymbol}")
    public String waypoint(@PathVariable("waypointSymbol") String symbol) {
        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(symbol);
        return "redirect:/galaxy/system/%s/map?target=%s".formatted(waypointSymbol.system(), waypointSymbol.waypoint());
    }

    @GetMapping("/galaxy/waypoint/{waypointSymbol}.json")
    @ResponseBody
    public ResponseEntity<Waypoint> getWaypointJson(@PathVariable("waypointSymbol") String symbol) {
        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(symbol);
        var waypoint = systemsApi.getWaypoint(waypointSymbol.system(), waypointSymbol.waypoint()).getData();
        return ResponseEntity.ok(waypoint);
    }

    @GetMapping("/galaxy/system/{system}/map")
    public String map(@PathVariable("system") String systemSymbol, @RequestParam(value = "target", required = false) String targetSymbol, Model model) throws JsonProcessingException {
        Objects.requireNonNull(systemSymbol);

        System system = systemsApi.getSystem(systemSymbol).getData();

        MapData.MapDataBuilder mapDataBuilder = MapData.builder();

        if (targetSymbol != null && !targetSymbol.isBlank()) {
            mapDataBuilder.target(targetSymbol);
        }

        List<MapWaypoint> shipWaypoints = getCurrentShipsInSystem(systemSymbol).stream()
            .map(GalaxyController::mapToShipWaypoint)
            .toList();

        List<MapWaypoint> mapWaypoints = system.getWaypoints()
            .stream()
            .map(waypoint -> new MapWaypoint(
                waypoint.getSymbol(),
                waypoint.getType().toString(),
                waypoint.getX(),
                waypoint.getY(),
                0,
                waypoint.getOrbits()
            ))
            .collect(Collectors.toList());

        mapWaypoints.addAll(shipWaypoints);

        mapDataBuilder
            .waypoints(mapWaypoints)
            .seed(SEED.toString());

        String mapDataJson = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(mapDataBuilder.build());

        model.addAttribute("map", mapDataJson);
        return "galaxy/map";
    }

    private List<Ship> getCurrentShipsInSystem(String systemSymbol) {
        // TODO: support paging in the frontend, if there are many ships this will probably slow everything to a crawl
        return StreamEx.of(fleetApi.getMyShips().iterator())
            .filter(ship -> ship.getNav().isInSystem(systemSymbol))
            .collect(Collectors.toList());
    }

    private static MapWaypoint mapToShipWaypoint(Ship s) {
        ShipNavRoute shipRoute = s.getNav().getRoute();
        ShipNavRouteWaypoint origin = shipRoute.getOrigin();

        return new MapWaypoint(s.getSymbol(), "SHIP", origin.getX(), origin.getY(), 100, origin.getSymbol());
    }
}
