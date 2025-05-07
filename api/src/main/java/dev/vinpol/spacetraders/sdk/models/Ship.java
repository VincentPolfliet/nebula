package dev.vinpol.spacetraders.sdk.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Ship details.
 */

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

    public Ship() {
    }

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

    public Ship withNav(Consumer<ShipNav> consumer) {
        ShipNav currentNav = getNav() != null ? getNav() : new ShipNav();
        consumer.accept(currentNav);
        return nav(currentNav);
    }

    public boolean isInOrbit() {
        return getNav().isInOrbit();
    }

    public boolean isNotInOrbit() {
        return !isInOrbit();
    }

    public boolean isInTransit() {
        return getNav().isInTransit();
    }

    public boolean isDocked() {
        return getNav().isDocked();
    }

    public boolean isAtLocation(String waypoint) {
        String currentLocation = nav.getWaypointSymbol();
        return Objects.equals(waypoint, currentLocation);
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

    public Ship withCooldown(Consumer<Cooldown> consumer) {
        Cooldown currentCooldown = getCooldown() != null ? getCooldown() : Cooldown.noCooldown(getSymbol());
        consumer.accept(currentCooldown);
        return cooldown(currentCooldown);
    }

    public boolean hasActiveCooldown() {
        return getCooldown().isActive();
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

    public Ship withCargo(Consumer<ShipCargo> consumer) {
        ShipCargo currentCargo = getCargo() != null ? getCargo() : new ShipCargo();
        consumer.accept(currentCargo);
        return cargo(currentCargo);
    }

    public boolean isCargoFull() {
        return cargo.isFull();
    }

    public boolean isCargoEmpty() {
        return cargo.isEmpty();
    }

    public Ship fuel(ShipFuel fuel) {
        this.fuel = fuel;
        return this;
    }

    public Ship withFuel(Consumer<ShipFuel> consumer) {
        ShipFuel currentFuel = getFuel() != null ? getFuel() : new ShipFuel();
        consumer.accept(currentFuel);
        return fuel(currentFuel);
    }

    public boolean isFuelFull() {
        return fuel.isFull();
    }

    public boolean isFuelEmpty() {
        return fuel.isEmpty();
    }

    public boolean isFuelInfinite() {
        return fuel.isInfinite();
    }

    public boolean isFuelNotInfinite() {
        return fuel.isNotInfinite();
    }

    public boolean considerFuelEmpty(double maxValue) {
        return fuel.shouldConsiderEmpty(maxValue);
    }

    public ShipRole getRole() {
        return getRegistration().getRole();
    }

    public String getSymbol() {
        return this.symbol;
    }

    public ShipRegistration getRegistration() {
        return this.registration;
    }

    public ShipNav getNav() {
        return this.nav;
    }

    public ShipCrew getCrew() {
        return this.crew;
    }

    public ShipFrame getFrame() {
        return this.frame;
    }

    public ShipReactor getReactor() {
        return this.reactor;
    }

    public ShipEngine getEngine() {
        return this.engine;
    }

    public Cooldown getCooldown() {
        return this.cooldown;
    }

    public List<ShipModule> getModules() {
        return this.modules;
    }

    public List<ShipMount> getMounts() {
        return this.mounts;
    }

    public ShipCargo getCargo() {
        return this.cargo;
    }

    public ShipFuel getFuel() {
        return this.fuel;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setRegistration(ShipRegistration registration) {
        this.registration = registration;
    }

    public void setNav(ShipNav nav) {
        this.nav = nav;
    }

    public void setCrew(ShipCrew crew) {
        this.crew = crew;
    }

    public void setFrame(ShipFrame frame) {
        this.frame = frame;
    }

    public void setReactor(ShipReactor reactor) {
        this.reactor = reactor;
    }

    public void setEngine(ShipEngine engine) {
        this.engine = engine;
    }

    public void setCooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
    }

    public void setModules(List<ShipModule> modules) {
        this.modules = modules;
    }

    public void setMounts(List<ShipMount> mounts) {
        this.mounts = mounts;
    }

    public void setCargo(ShipCargo cargo) {
        this.cargo = cargo;
    }

    public void setFuel(ShipFuel fuel) {
        this.fuel = fuel;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Ship)) return false;
        final Ship other = (Ship) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$symbol = this.getSymbol();
        final Object other$symbol = other.getSymbol();
        if (this$symbol == null ? other$symbol != null : !this$symbol.equals(other$symbol)) return false;
        final Object this$registration = this.getRegistration();
        final Object other$registration = other.getRegistration();
        if (this$registration == null ? other$registration != null : !this$registration.equals(other$registration))
            return false;
        final Object this$nav = this.getNav();
        final Object other$nav = other.getNav();
        if (this$nav == null ? other$nav != null : !this$nav.equals(other$nav)) return false;
        final Object this$crew = this.getCrew();
        final Object other$crew = other.getCrew();
        if (this$crew == null ? other$crew != null : !this$crew.equals(other$crew)) return false;
        final Object this$frame = this.getFrame();
        final Object other$frame = other.getFrame();
        if (this$frame == null ? other$frame != null : !this$frame.equals(other$frame)) return false;
        final Object this$reactor = this.getReactor();
        final Object other$reactor = other.getReactor();
        if (this$reactor == null ? other$reactor != null : !this$reactor.equals(other$reactor)) return false;
        final Object this$engine = this.getEngine();
        final Object other$engine = other.getEngine();
        if (this$engine == null ? other$engine != null : !this$engine.equals(other$engine)) return false;
        final Object this$cooldown = this.getCooldown();
        final Object other$cooldown = other.getCooldown();
        if (this$cooldown == null ? other$cooldown != null : !this$cooldown.equals(other$cooldown)) return false;
        final Object this$modules = this.getModules();
        final Object other$modules = other.getModules();
        if (this$modules == null ? other$modules != null : !this$modules.equals(other$modules)) return false;
        final Object this$mounts = this.getMounts();
        final Object other$mounts = other.getMounts();
        if (this$mounts == null ? other$mounts != null : !this$mounts.equals(other$mounts)) return false;
        final Object this$cargo = this.getCargo();
        final Object other$cargo = other.getCargo();
        if (this$cargo == null ? other$cargo != null : !this$cargo.equals(other$cargo)) return false;
        final Object this$fuel = this.getFuel();
        final Object other$fuel = other.getFuel();
        if (this$fuel == null ? other$fuel != null : !this$fuel.equals(other$fuel)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Ship;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $symbol = this.getSymbol();
        result = result * PRIME + ($symbol == null ? 43 : $symbol.hashCode());
        final Object $registration = this.getRegistration();
        result = result * PRIME + ($registration == null ? 43 : $registration.hashCode());
        final Object $nav = this.getNav();
        result = result * PRIME + ($nav == null ? 43 : $nav.hashCode());
        final Object $crew = this.getCrew();
        result = result * PRIME + ($crew == null ? 43 : $crew.hashCode());
        final Object $frame = this.getFrame();
        result = result * PRIME + ($frame == null ? 43 : $frame.hashCode());
        final Object $reactor = this.getReactor();
        result = result * PRIME + ($reactor == null ? 43 : $reactor.hashCode());
        final Object $engine = this.getEngine();
        result = result * PRIME + ($engine == null ? 43 : $engine.hashCode());
        final Object $cooldown = this.getCooldown();
        result = result * PRIME + ($cooldown == null ? 43 : $cooldown.hashCode());
        final Object $modules = this.getModules();
        result = result * PRIME + ($modules == null ? 43 : $modules.hashCode());
        final Object $mounts = this.getMounts();
        result = result * PRIME + ($mounts == null ? 43 : $mounts.hashCode());
        final Object $cargo = this.getCargo();
        result = result * PRIME + ($cargo == null ? 43 : $cargo.hashCode());
        final Object $fuel = this.getFuel();
        result = result * PRIME + ($fuel == null ? 43 : $fuel.hashCode());
        return result;
    }

    public String toString() {
        return "Ship(symbol=" + this.getSymbol() + ", registration=" + this.getRegistration() + ", nav=" + this.getNav() + ", crew=" + this.getCrew() + ", frame=" + this.getFrame() + ", reactor=" + this.getReactor() + ", engine=" + this.getEngine() + ", cooldown=" + this.getCooldown() + ", modules=" + this.getModules() + ", mounts=" + this.getMounts() + ", cargo=" + this.getCargo() + ", fuel=" + this.getFuel() + ")";
    }
}
