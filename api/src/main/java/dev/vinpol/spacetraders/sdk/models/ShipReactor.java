package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

/**
 * The reactor of the ship. The reactor is responsible for powering the ship&#39;s systems and weapons.
 */
@Data
public class ShipReactor {
    /**
     * Symbol of the reactor.
     */

    public enum SymbolEnum {
        SOLAR_I("REACTOR_SOLAR_I"),

        FUSION_I("REACTOR_FUSION_I"),

        FISSION_I("REACTOR_FISSION_I"),

        CHEMICAL_I("REACTOR_CHEMICAL_I"),

        ANTIMATTER_I("REACTOR_ANTIMATTER_I");

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
    private Integer powerOutput;
    private double integrity;
    private int quality;
    private ShipRequirements requirements;

    public ShipReactor symbol(SymbolEnum symbol) {
        this.symbol = symbol;
        return this;
    }

    public ShipReactor name(String name) {
        this.name = name;
        return this;
    }

    public ShipReactor description(String description) {
        this.description = description;
        return this;
    }

    public ShipReactor condition(double condition) {
        this.condition = condition;
        return this;
    }


    public ShipReactor integrity(double integrity) {
        this.integrity = integrity;
        return this;
    }

    public ShipReactor powerOutput(Integer powerOutput) {
        this.powerOutput = powerOutput;
        return this;
    }


    public ShipReactor requirements(ShipRequirements requirements) {
        this.requirements = requirements;
        return this;
    }
}
