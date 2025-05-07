package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.util.Objects;

/**
 * The engine determines how quickly a ship travels between waypoints.
 */

@Data
public class ShipEngine {
    /**
     * The symbol of the engine.
     */

    public enum SymbolEnum {
        IMPULSE_DRIVE_I("ENGINE_IMPULSE_DRIVE_I"),

        ION_DRIVE_I("ENGINE_ION_DRIVE_I"),

        ION_DRIVE_II("ENGINE_ION_DRIVE_II"),

        HYPER_DRIVE_I("ENGINE_HYPER_DRIVE_I");

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
    private double condition;
    private Integer speed;
    private double integrity;
    private int quality;
    private ShipRequirements requirements;

    public ShipEngine() {
    }

    public ShipEngine symbol(SymbolEnum symbol) {

        this.symbol = symbol;
        return this;
    }

    /**
     * The symbol of the engine.
     *
     * @return symbol
     **/


    public SymbolEnum getSymbol() {
        return symbol;
    }


    public void setSymbol(SymbolEnum symbol) {
        this.symbol = symbol;
    }


    public ShipEngine name(String name) {

        this.name = name;
        return this;
    }

    /**
     * The name of the engine.
     *
     * @return name
     **/


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public ShipEngine description(String description) {

        this.description = description;
        return this;
    }

    /**
     * The description of the engine.
     *
     * @return description
     **/


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public ShipEngine condition(double condition) {

        this.condition = condition;
        return this;
    }

    /**
     * Condition is a range of 0 to 100 where 0 is completely worn out and 100 is brand new.
     * minimum: 0
     * maximum: 100
     *
     * @return condition
     **/


    public double getCondition() {
        return condition;
    }


    public void setCondition(double condition) {
        this.condition = condition;
    }


    public ShipEngine speed(Integer speed) {

        this.speed = speed;
        return this;
    }

    /**
     * The speed stat of this engine. The higher the speed, the faster a ship can travel from one point to another. Reduces the time of arrival when navigating the ship.
     * minimum: 1
     *
     * @return speed
     **/


    public Integer getSpeed() {
        return speed;
    }


    public void setSpeed(Integer speed) {
        this.speed = speed;
    }


    public ShipEngine requirements(ShipRequirements requirements) {

        this.requirements = requirements;
        return this;
    }

    /**
     * Get requirements
     *
     * @return requirements
     **/


    public ShipRequirements getRequirements() {
        return requirements;
    }


    public void setRequirements(ShipRequirements requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShipEngine shipEngine = (ShipEngine) o;
        return Objects.equals(this.symbol, shipEngine.symbol) &&
               Objects.equals(this.name, shipEngine.name) &&
               Objects.equals(this.description, shipEngine.description) &&
               Objects.equals(this.condition, shipEngine.condition) &&
               Objects.equals(this.speed, shipEngine.speed) &&
               Objects.equals(this.requirements, shipEngine.requirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, name, description, condition, speed, requirements);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ShipEngine {\n");
        sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
        sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
        sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}

