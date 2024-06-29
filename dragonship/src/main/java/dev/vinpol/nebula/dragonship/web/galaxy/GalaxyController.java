package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.geo.GridXY;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class GalaxyController {

    public static final int OPTIMAL_NUMBERS = 36;


    private final FleetApi fleetApi;
    private final SystemsApi systemsApi;

    private final MapDataCache mapDataCache;

    private final ObjectMapper objectMapper;

    public GalaxyController(FleetApi fleetApi, SystemsApi systemsApi, MapDataCache mapDataCache, ObjectMapper objectMapper) {
        this.fleetApi = fleetApi;
        this.systemsApi = systemsApi;
        this.mapDataCache = mapDataCache;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/galaxy")
    public String galaxy() {
        return "redirect:/galaxy/systems";
    }

    @GetMapping("/galaxy/systems")
    public String getGalaxySystems(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "total", defaultValue = "10") int total,
                                   Model model) {
        HtmlPage content = new HtmlPage();
        content.setTitle("Galaxy");
        model.addAttribute("page", content);

        GetSystems200Response systems = systemsApi.getSystems(page, total);
        PagingUtils.setMetaOnModel(model, systems.getMeta());
        model.addAttribute("systems", systems.getData());
        return "galaxy/systems";
    }

    @GetMapping("/galaxy/system/{system}/markets.json")
    @ResponseBody
    public ResponseEntity<Object> getMarketsOfSystem(@PathVariable("system") String symbol) {
        return ResponseEntity.ok(systemsApi.getSystemWaypoints(symbol, 1, 10, null, WaypointTraitSymbol.MARKETPLACE));
    }

    @GetMapping("/galaxy/waypoint/{waypointSymbol}")
    public String waypoint(@PathVariable("waypointSymbol") String symbol, @RequestParam(value = "map", required = false, defaultValue = "true") boolean map) {
        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(symbol);
        return "redirect:/galaxy/system/%s?target=%s".formatted(waypointSymbol.system() + (map ? "/map" : ""), waypointSymbol.waypoint());
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

        MapData mapData = Optional.ofNullable(mapDataCache.get(systemSymbol))
            // clone because mutable and not threadsafe
            .map(MapDataCloner::clone)
            .orElseGet(() -> {
                MapData calculateMapData = calculateMapData(systemSymbol, system);
                mapDataCache.set(systemSymbol, calculateMapData);
                return calculateMapData;
            });

        if (targetSymbol != null && !targetSymbol.isEmpty()) {
            mapData.setTarget(targetSymbol);
        }

        String mapDataJson = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(mapData);

        model.addAttribute("map", mapDataJson);
        return "galaxy/map";
    }

    private MapData calculateMapData(String systemSymbol, System system) {
        MapData mapData = new MapData();

        Map<SystemWaypoint, List<GridXY>> availableParking = calculateAvailableParking(system);
        Map<SystemWaypoint, List<GridXY>> availableOrbits = calculateAvailableOrbits(system);

        mapData.setWaypoints(
            system.getWaypoints()
                .stream()
                .map(s -> {
                    if (s.isInOrbit()) {
                        List<GridXY> availableOrbitsForWaypoint = availableOrbits.get(s);

                        GridXY coordinate = popRandom(availableOrbitsForWaypoint);
                        return new WayPoint(s.getSymbol(), s.getType().toString(), coordinate.x(), coordinate.y());
                    }

                    return new WayPoint(s.getSymbol(), s.getType().toString(), s.getX(), s.getY());
                })
                .toList()
        );

        for (Ship ship : getCurrentShipsInSystem(systemSymbol)) {
            SystemWaypoint shipSystemWayPoint = system.getWaypoints().stream().filter(s -> s.getSymbol().equals(ship.getNav().getWaypointSymbol())).findFirst().orElseThrow();
            List<GridXY> parkingOptions = availableParking.get(shipSystemWayPoint);

            GridXY parking = popRandom(parkingOptions);

            mapData.addWayPoint(
                new WayPoint(
                    ship.getSymbol(),
                    "SHIP",
                    parking.x(),
                    parking.y(),
                    100
                )
            );
        }

        return mapData;
    }

    private static GridXY popRandom(List<GridXY> coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            // coordinate should not be empty or null
            throw new IllegalStateException("Calculated coordinates can not be null or empty when popping");
        }

        int parkingOptionIndex = ThreadLocalRandom.current().nextInt(0, coordinates.size());
        return coordinates.remove(parkingOptionIndex);
    }

    private Map<SystemWaypoint, List<GridXY>> calculateAvailableOrbits(System system) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        return system.getWaypoints()
            .stream()
            // this random introduces different rings of orbitals
            .collect(Collectors.toMap(Function.identity(), waypoint -> calculateCoordinatesOfWayPoint(waypoint, random.nextInt(8, 12), waypoint.getOrbitals().size() + 1)));
    }

    private Map<SystemWaypoint, List<GridXY>> calculateAvailableParking(System system) {
        return system.getWaypoints()
            .stream()
            .collect(Collectors.toMap(Function.identity(), waypoint -> calculateCoordinatesOfWayPoint(waypoint, 0.25, 8)));
    }

    private static List<GridXY> calculateCoordinatesOfWayPoint(SystemWaypoint systemWaypoint, double radius, int wantedNumberOfPoints) {
        List<GridXY> coordinates = calculateCoordinates(systemWaypoint.getX(), systemWaypoint.getY(), radius, Math.max(wantedNumberOfPoints, OPTIMAL_NUMBERS));
        // this random causes not the same coordinates to not be assigned multiple times on different planets,
        // e.g. planet 1 would have index 0, planet 2 would have index 0, planet 3 would have index 0, ... when there are only a few orbitals
        Collections.shuffle(coordinates);

        while (coordinates.size() > wantedNumberOfPoints) {
            coordinates.removeLast();
        }

        return coordinates;
    }

    private List<Ship> getCurrentShipsInSystem(String systemSymbol) {
        // TODO: support paging in the frontend, if there are many ships this will probably slow everything to a crawl
        return StreamEx.of(fleetApi.getMyShips().iterator())
            .filter(ship -> ship.getNav().isInSystem(systemSymbol))
            .collect(Collectors.toList());
    }


    private static List<GridXY> calculateCoordinates(double centerX, double centerY, double radius, int numPoints) {
        List<GridXY> coordinates = new ArrayList<>();

        for (int i = 0; i < numPoints; ++i) {
            double angle = Math.toRadians(((double) 360 / numPoints) * i);
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            coordinates.add(new GridXY(x, y));
        }

        return coordinates;
    }
}
