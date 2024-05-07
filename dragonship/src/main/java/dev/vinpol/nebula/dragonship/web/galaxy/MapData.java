package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.vinpol.spacetraders.sdk.models.System;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class MapData {

    @JsonProperty("waypoints")
    private List<WayPoint> waypoints = new ArrayList<>();

    void addWayPoint(WayPoint wayPoint) {
        Objects.requireNonNull(wayPoint);

        waypoints.add(wayPoint);
    }

    public static MapData ofSystem(System system) {
        MapData mapData = new MapData();
        mapData.setWaypoints(
            system.getWaypoints()
                .stream()
                .map(s -> new WayPoint(s.getSymbol(), s.getType().getValue(), s.getX(), s.getY()))
                .toList()
        );

        return mapData;
    }
}
