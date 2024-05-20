package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

public class MapData {

    private final static Map<String, Integer> PRIORITY_MAP = new HashMap<>();

    static {
        PRIORITY_MAP.put("SHIP", 100);
    }

    @JsonProperty("target")
    private String target;

    @JsonProperty("waypoints")
    private List<WayPoint> waypoints = new ArrayList<>();

    public void addWayPoint(WayPoint wayPoint) {
        Objects.requireNonNull(wayPoint);

        waypoints.add(wayPoint);
    }

    public void setWaypoints(List<WayPoint> waypoints) {
        this.waypoints.clear();
        this.waypoints.addAll(waypoints);
    }

    public List<WayPoint> getWaypoints() {
        return waypoints;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @JsonProperty("types")
    public Set<MapItemType> getTypes() {
        return waypoints.stream()
            .map(w -> new MapItemType(w.type(), w.type()))
            .sorted((o1, o2) -> {
                Integer leftPriority = PRIORITY_MAP.getOrDefault(o1.type(), 0);
                Integer rightPriority = PRIORITY_MAP.getOrDefault(o2.type(), 0);

                return Integer.compare(rightPriority, leftPriority);
            })
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
