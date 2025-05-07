package dev.vinpol.spacetraders.sdk.models;

import java.util.List;

public final class ShipBuilder {
    private String symbol;
    private ShipRegistration registration;
    private ShipNav nav;
    private ShipCrew crew;
    private ShipFrame frame;
    private ShipReactor reactor;
    private ShipEngine engine;
    private Cooldown cooldown;
    private List<ShipModule> modules;
    private List<ShipMount> mounts;
    private ShipCargo cargo;
    private ShipFuel fuel;

    public ShipBuilder() {
    }

    public ShipBuilder withSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public ShipBuilder withRegistration(ShipRegistration registration) {
        this.registration = registration;
        return this;
    }

    public ShipBuilder withNav(ShipNav nav) {
        this.nav = nav;
        return this;
    }

    public ShipBuilder withCrew(ShipCrew crew) {
        this.crew = crew;
        return this;
    }

    public ShipBuilder withFrame(ShipFrame frame) {
        this.frame = frame;
        return this;
    }

    public ShipBuilder withReactor(ShipReactor reactor) {
        this.reactor = reactor;
        return this;
    }

    public ShipBuilder withEngine(ShipEngine engine) {
        this.engine = engine;
        return this;
    }

    public ShipBuilder withCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public ShipBuilder withModules(List<ShipModule> modules) {
        this.modules = modules;
        return this;
    }

    public ShipBuilder withMounts(List<ShipMount> mounts) {
        this.mounts = mounts;
        return this;
    }

    public ShipBuilder withCargo(ShipCargo cargo) {
        this.cargo = cargo;
        return this;
    }

    public ShipBuilder withFuel(ShipFuel fuel) {
        this.fuel = fuel;
        return this;
    }

    public Ship build() {
        Ship ship = new Ship();
        ship.setSymbol(symbol);
        ship.setRegistration(registration);
        ship.setNav(nav);
        ship.setCrew(crew);
        ship.setFrame(frame);
        ship.setReactor(reactor);
        ship.setEngine(engine);
        ship.setCooldown(cooldown);
        ship.setModules(modules);
        ship.setMounts(mounts);
        ship.setCargo(cargo);
        ship.setFuel(fuel);
        return ship;
    }
}
