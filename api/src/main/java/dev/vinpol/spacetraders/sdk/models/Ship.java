package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Ship details.
 */

@Data
public class Ship {
    private String symbol;
    private ShipRegistration registration;
    private ShipNav nav;
    private ShipCrew crew;
    private ShipFrame frame;
    private ShipReactor reactor;
    private ShipEngine engine;
    private Cooldown cooldown;
    private List<ShipModule> modules = new ArrayList<>();
    private List<ShipMount> mounts = new ArrayList<>();
    private ShipCargo cargo;
    private ShipFuel fuel;

    public Ship symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public Ship registration(ShipRegistration registration) {
        this.registration = registration;
        return this;
    }

    public Ship nav(ShipNav nav) {
        this.nav = nav;
        return this;
    }

    public boolean isInOrbit() {
        return getNav().isInOrbit();
    }

    public boolean isInTransit() {
        return getNav().isInTransit();
    }

    public boolean isDocked() {
        return getNav().isDocked();
    }

    public Ship crew(ShipCrew crew) {
        this.crew = crew;
        return this;
    }

    public Ship frame(ShipFrame frame) {
        this.frame = frame;
        return this;
    }

    public Ship reactor(ShipReactor reactor) {
        this.reactor = reactor;
        return this;
    }

    public Ship engine(ShipEngine engine) {
        this.engine = engine;
        return this;
    }

    public Ship cooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public boolean hasActiveCooldown() {
        return getCooldown().getExpiration() != null;
    }

    public Ship modules(List<ShipModule> modules) {
        this.modules = modules;
        return this;
    }

    public Ship addModulesItem(ShipModule modulesItem) {
        this.modules.add(modulesItem);
        return this;
    }

    public Ship mounts(List<ShipMount> mounts) {
        this.mounts = mounts;
        return this;
    }

    public Ship addMountsItem(ShipMount mountsItem) {
        this.mounts.add(mountsItem);
        return this;
    }

    public Ship cargo(ShipCargo cargo) {
        this.cargo = cargo;
        return this;
    }

    public boolean isCargoFull() {
        return cargo.isFull();
    }

    public Ship fuel(ShipFuel fuel) {
        this.fuel = fuel;
        return this;
    }

    public boolean isFuelFull() {
        return fuel.isFull();
    }

    public boolean isFuelEmpty() {
        return fuel.isEmpty();
    }

    public boolean considerFuelEmpty(double maxValue) {
        return fuel.shouldConsiderEmpty(maxValue);
    }

    public ShipRole getRole() {
        return getRegistration().getRole();
    }
}
