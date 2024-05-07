package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * SystemWaypoint
 */
@Data
public class SystemWaypoint {
    private String symbol;
    private WaypointType type;
    /**
     * Relative position of the waypoint on the system&#39;s x axis. This is not an absolute position in the universe.
     */
    private Integer x;
    /**
     * Relative position of the waypoint on the system&#39;s y axis. This is not an absolute position in the universe.
     */
    private Integer y;

    /**
     * Waypoints that orbit this waypoint.
     */
    private List<WaypointOrbital> orbitals = new ArrayList<>();

    /**
     * The symbol of the parent waypoint, if this waypoint is in orbit around another waypoint. Otherwise this value is undefined.
     */
    private String orbits;

    public boolean isInOrbit() {
        return orbits != null && !orbits.isEmpty();
    }

    public SystemWaypoint symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public SystemWaypoint type(WaypointType type) {
        this.type = type;
        return this;
    }

    public SystemWaypoint x(Integer x) {
        this.x = x;
        return this;
    }


    public SystemWaypoint y(Integer y) {
        this.y = y;
        return this;
    }


    public SystemWaypoint orbitals(List<WaypointOrbital> orbitals) {
        this.orbitals = orbitals;
        return this;
    }

    public SystemWaypoint addOrbitalsItem(WaypointOrbital orbitalsItem) {
        if (this.orbitals == null) {
            this.orbitals = new ArrayList<>();
        }

        this.orbitals.add(orbitalsItem);
        return this;
    }


    public SystemWaypoint orbits(String orbits) {
        this.orbits = orbits;
        return this;
    }
}

