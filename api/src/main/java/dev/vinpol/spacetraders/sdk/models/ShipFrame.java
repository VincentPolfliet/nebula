/*
 * SpaceTraders API
 * SpaceTraders is an open-universe game and learning platform that offers a set of HTTP endpoints to control a fleet of ships and explore a multiplayer universe.  The API is documented using [OpenAPI](https://github.com/SpaceTradersAPI/api-docs). You can send your first request right here in your browser to check the status of the game server.  ```json http {   \"method\": \"GET\",   \"url\": \"https://api.spacetraders.io/v2\", } ```  Unlike a traditional game, SpaceTraders does not have a first-party client or app to play the game. Instead, you can use the API to build your own client, write a script to automate your ships, or try an app built by the community.  We have a [Discord channel](https://discord.com/invite/jh6zurdWk5) where you can share your projects, ask questions, and get help from other players.
 *
 * The version of the OpenAPI document: 2.0.0
 * Contact: joel@spacetraders.io
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package dev.vinpol.spacetraders.sdk.models;

import java.util.Objects;
import java.util.Arrays;


import com.fasterxml.jackson.annotation.JsonValue;
import dev.vinpol.spacetraders.sdk.models.ShipRequirements;
import java.io.IOException;

/**
 * The frame of the ship. The frame determines the number of modules and mounting points of the ship, as well as base fuel capacity. As the condition of the frame takes more wear, the ship will become more sluggish and less maneuverable.
 */

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

  public static final String SERIALIZED_NAME_SYMBOL = "symbol";

  private SymbolEnum symbol;

  public static final String SERIALIZED_NAME_NAME = "name";

  private String name;

  public static final String SERIALIZED_NAME_DESCRIPTION = "description";

  private String description;

  public static final String SERIALIZED_NAME_CONDITION = "condition";

  private Integer condition;

  public static final String SERIALIZED_NAME_MODULE_SLOTS = "moduleSlots";

  private Integer moduleSlots;

  public static final String SERIALIZED_NAME_MOUNTING_POINTS = "mountingPoints";

  private Integer mountingPoints;

  public static final String SERIALIZED_NAME_FUEL_CAPACITY = "fuelCapacity";

  private Integer fuelCapacity;

  public static final String SERIALIZED_NAME_REQUIREMENTS = "requirements";

  private ShipRequirements requirements;

  public ShipFrame() {
  }

  public ShipFrame symbol(SymbolEnum symbol) {

    this.symbol = symbol;
    return this;
  }

   /**
   * Symbol of the frame.
   * @return symbol
  **/


  public SymbolEnum getSymbol() {
    return symbol;
  }


  public void setSymbol(SymbolEnum symbol) {
    this.symbol = symbol;
  }


  public ShipFrame name(String name) {

    this.name = name;
    return this;
  }

   /**
   * Name of the frame.
   * @return name
  **/


  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public ShipFrame description(String description) {

    this.description = description;
    return this;
  }

   /**
   * Description of the frame.
   * @return description
  **/


  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public ShipFrame condition(Integer condition) {

    this.condition = condition;
    return this;
  }

   /**
   * Condition is a range of 0 to 100 where 0 is completely worn out and 100 is brand new.
   * minimum: 0
   * maximum: 100
   * @return condition
  **/


  public Integer getCondition() {
    return condition;
  }


  public void setCondition(Integer condition) {
    this.condition = condition;
  }


  public ShipFrame moduleSlots(Integer moduleSlots) {

    this.moduleSlots = moduleSlots;
    return this;
  }

   /**
   * The amount of slots that can be dedicated to modules installed in the ship. Each installed module take up a number of slots, and once there are no more slots, no new modules can be installed.
   * minimum: 0
   * @return moduleSlots
  **/


  public Integer getModuleSlots() {
    return moduleSlots;
  }


  public void setModuleSlots(Integer moduleSlots) {
    this.moduleSlots = moduleSlots;
  }


  public ShipFrame mountingPoints(Integer mountingPoints) {

    this.mountingPoints = mountingPoints;
    return this;
  }

   /**
   * The amount of slots that can be dedicated to mounts installed in the ship. Each installed mount takes up a number of points, and once there are no more points remaining, no new mounts can be installed.
   * minimum: 0
   * @return mountingPoints
  **/


  public Integer getMountingPoints() {
    return mountingPoints;
  }


  public void setMountingPoints(Integer mountingPoints) {
    this.mountingPoints = mountingPoints;
  }


  public ShipFrame fuelCapacity(Integer fuelCapacity) {

    this.fuelCapacity = fuelCapacity;
    return this;
  }

   /**
   * The maximum amount of fuel that can be stored in this ship. When refueling, the ship will be refueled to this amount.
   * minimum: 0
   * @return fuelCapacity
  **/


  public Integer getFuelCapacity() {
    return fuelCapacity;
  }


  public void setFuelCapacity(Integer fuelCapacity) {
    this.fuelCapacity = fuelCapacity;
  }


  public ShipFrame requirements(ShipRequirements requirements) {

    this.requirements = requirements;
    return this;
  }

   /**
   * Get requirements
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
    ShipFrame shipFrame = (ShipFrame) o;
    return Objects.equals(this.symbol, shipFrame.symbol) &&
        Objects.equals(this.name, shipFrame.name) &&
        Objects.equals(this.description, shipFrame.description) &&
        Objects.equals(this.condition, shipFrame.condition) &&
        Objects.equals(this.moduleSlots, shipFrame.moduleSlots) &&
        Objects.equals(this.mountingPoints, shipFrame.mountingPoints) &&
        Objects.equals(this.fuelCapacity, shipFrame.fuelCapacity) &&
        Objects.equals(this.requirements, shipFrame.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, name, description, condition, moduleSlots, mountingPoints, fuelCapacity, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipFrame {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    moduleSlots: ").append(toIndentedString(moduleSlots)).append("\n");
    sb.append("    mountingPoints: ").append(toIndentedString(mountingPoints)).append("\n");
    sb.append("    fuelCapacity: ").append(toIndentedString(fuelCapacity)).append("\n");
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

