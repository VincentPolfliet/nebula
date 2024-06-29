package dev.vinpol.spacetraders.sdk.models;

import java.util.Objects;
import java.util.Arrays;


import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.vinpol.spacetraders.sdk.models.Chart;
import dev.vinpol.spacetraders.sdk.models.WaypointFaction;
import dev.vinpol.spacetraders.sdk.models.WaypointModifier;
import dev.vinpol.spacetraders.sdk.models.WaypointOrbital;
import dev.vinpol.spacetraders.sdk.models.WaypointTrait;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Waypoint {
    private String symbol;
    private WaypointType type;
    private String systemSymbol;
    private Integer x;
    private Integer y;
    private List<WaypointOrbital> orbitals = new ArrayList<>();
    private String orbits;
    private WaypointFaction faction;
    private List<WaypointTrait> traits = new ArrayList<>();
    private List<WaypointModifier> modifiers;
    private Chart chart;
    private Boolean isUnderConstruction;

    public Waypoint() {
    }

    public Waypoint symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Waypoint type(WaypointType type) {
        this.type = type;
        return this;
    }

    public Waypoint systemSymbol(String systemSymbol) {
        this.systemSymbol = systemSymbol;
        return this;
    }

    public Waypoint x(Integer x) {
        this.x = x;
        return this;
    }

    public Waypoint y(Integer y) {
        this.y = y;
        return this;
    }

    public Waypoint orbitals(List<WaypointOrbital> orbitals) {
        this.orbitals = orbitals;
        return this;
    }

    public Waypoint addOrbitalsItem(WaypointOrbital orbitalsItem) {
        if (this.orbitals == null) {
            this.orbitals = new ArrayList<>();
        }

        this.orbitals.add(orbitalsItem);
        return this;
    }

    public Waypoint orbits(String orbits) {
        this.orbits = orbits;
        return this;
    }


    public Waypoint faction(WaypointFaction faction) {
        this.faction = faction;
        return this;
    }

    public Waypoint traits(List<WaypointTrait> traits) {
        this.traits = traits;
        return this;
    }

    public Waypoint addTraitsItem(WaypointTrait traitsItem) {
        if (this.traits == null) {
            this.traits = new ArrayList<>();
        }

        this.traits.add(traitsItem);
        return this;
    }

    @JsonIgnore
    public boolean isMarket() {
        return traits.stream()
            .anyMatch(t -> t.getSymbol() == WaypointTraitSymbol.MARKETPLACE);
    }

    public Waypoint modifiers(List<WaypointModifier> modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    public Waypoint addModifiersItem(WaypointModifier modifiersItem) {
        if (this.modifiers == null) {
            this.modifiers = new ArrayList<>();
        }

        this.modifiers.add(modifiersItem);
        return this;
    }

    public Waypoint chart(Chart chart) {
        this.chart = chart;
        return this;
    }

    public Waypoint isUnderConstruction(Boolean isUnderConstruction) {
        this.isUnderConstruction = isUnderConstruction;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Waypoint waypoint = (Waypoint) o;
        return Objects.equals(getSymbol(), waypoint.getSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSymbol());
    }

    @Override
    public String toString() {
        return "Waypoint{" +
            "symbol='" + symbol + '\'' +
            '}';
    }

    public Waypoint xy(int x, int y) {
        setX(x);
        setY(y);
        return this;
    }
}

