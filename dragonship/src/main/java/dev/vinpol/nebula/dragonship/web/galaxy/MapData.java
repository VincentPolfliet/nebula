package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;
import java.util.stream.Collectors;

public record MapData(
    @JsonProperty("target")
    String target,
    @JsonProperty("waypoints")
    List<MapWaypoint> waypoints,
    @JsonProperty("seed")
    String seed
) {
    @JsonProperty("types")
    public Set<MapItemType> types() {
        return waypoints.stream()
            .sorted(Comparator.comparing(MapWaypoint::priority))
            .map(w -> new MapItemType(w.type(), w.type()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static MapDataBuilder builder() {
        return new MapDataBuilder();
    }

    public static final class MapDataBuilder {
        private String target;
        private String seed;
        private List<MapWaypoint> waypoints;

        private MapDataBuilder() {
        }

        public MapDataBuilder target(String target) {
            this.target = target;
            return this;
        }

        public MapDataBuilder waypoints(List<MapWaypoint> waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        public MapDataBuilder seed(String seed) {
            this.seed = seed;
            return this;
        }

        public MapData build() {
            return new MapData(target, waypoints, seed);
        }
    }
}
