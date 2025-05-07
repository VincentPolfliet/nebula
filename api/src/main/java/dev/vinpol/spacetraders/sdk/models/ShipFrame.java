package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

/**
 * The frame of the ship. The frame determines the number of modules and mounting points of the ship, as well as base fuel capacity. As the condition of the frame takes more wear, the ship will become more sluggish and less maneuverable.
 */

@Data
public class ShipFrame {
    /**
     * Symbol of the frame.
     */

    public enum SymbolEnum {
        PROBE("FRAME_PROBE"),

        DRONE("FRAME_DRONE"),

        INTERCEPTOR("FRAME_INTERCEPTOR"),

        RACER("FRAME_RACER"),

        FIGHTER("FRAME_FIGHTER"),

        FRIGATE("FRAME_FRIGATE"),

        SHUTTLE("FRAME_SHUTTLE"),

        EXPLORER("FRAME_EXPLORER"),

        MINER("FRAME_MINER"),

        LIGHT_FREIGHTER("FRAME_LIGHT_FREIGHTER"),

        HEAVY_FREIGHTER("FRAME_HEAVY_FREIGHTER"),

        TRANSPORT("FRAME_TRANSPORT"),

        DESTROYER("FRAME_DESTROYER"),

        CRUISER("FRAME_CRUISER"),

        CARRIER("FRAME_CARRIER");

        private String value;

        SymbolEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static SymbolEnum fromValue(String value) {
            for (SymbolEnum b : SymbolEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

    }

    private SymbolEnum symbol;
    private String name;
    private String description;

    /**
     * Condition is a range of 0 to 100 where 0 is completely worn out and 100 is brand new.
     * minimum: 0
     * maximum: 1
     **/
    private double condition;

    /**
     * The amount of slots that can be dedicated to modules installed in the ship. Each installed module take up a number of slots, and once there are no more slots, no new modules can be installed.
     * minimum: 0
     **/
    private int moduleSlots;

    /**
     * The amount of slots that can be dedicated to mounts installed in the ship. Each installed mount takes up a number of points, and once there are no more points remaining, no new mounts can be installed.
     * minimum: 0
     **/
    private int mountingPoints;
    private int fuelCapacity;
    private double integrity;
    private int quality;
    private ShipRequirements requirements;

    public ShipFrame symbol(SymbolEnum symbol) {
        this.symbol = symbol;
        return this;
    }

    public ShipFrame name(String name) {
        this.name = name;
        return this;
    }

    public ShipFrame description(String description) {
        this.description = description;
        return this;
    }

    public ShipFrame condition(double condition) {
        this.condition = condition;
        return this;
    }

    public ShipFrame moduleSlots(Integer moduleSlots) {
        this.moduleSlots = moduleSlots;
        return this;
    }

    public ShipFrame mountingPoints(Integer mountingPoints) {
        this.mountingPoints = mountingPoints;
        return this;
    }

    public ShipFrame integrity(double integrity) {
        this.integrity = integrity;
        return this;
    }

    public ShipFrame fuelCapacity(Integer fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
        return this;
    }

    public ShipFrame requirements(ShipRequirements requirements) {
        this.requirements = requirements;
        return this;
    }
}

