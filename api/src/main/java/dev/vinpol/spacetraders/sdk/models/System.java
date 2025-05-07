package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * System
 */

@Data
public class System {
    private String name;
    private String symbol;
    private String sectorSymbol;
    private SystemType type;
    private String constellation;
    private Integer x;
    private Integer y;
    private List<SystemWaypoint> waypoints = new ArrayList<>();
    private List<SystemFaction> factions = new ArrayList<>();

    public System() {
    }

    public System symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }


    public System sectorSymbol(String sectorSymbol) {
        this.sectorSymbol = sectorSymbol;
        return this;
    }


    public System type(SystemType type) {
        this.type = type;
        return this;
    }

    public System constellation(String constellation) {
        this.constellation = constellation;
        return this;
    }

    public System x(Integer x) {
        this.x = x;
        return this;
    }


    public System y(Integer y) {
        this.y = y;
        return this;
    }


    public System waypoints(List<SystemWaypoint> waypoints) {
        this.waypoints = waypoints;
        return this;
    }

    public System addWaypointsItem(SystemWaypoint waypointsItem) {
        if (this.waypoints == null) {
            this.waypoints = new ArrayList<>();
        }

        this.waypoints.add(waypointsItem);
        return this;
    }


    public System factions(List<SystemFaction> factions) {
        this.factions = factions;
        return this;
    }

    public System addFactionsItem(SystemFaction factionsItem) {
        if (this.factions == null) {
            this.factions = new ArrayList<>();
        }

        this.factions.add(factionsItem);
        return this;
    }
}

