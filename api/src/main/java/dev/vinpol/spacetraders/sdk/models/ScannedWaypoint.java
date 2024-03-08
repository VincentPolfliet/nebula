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


import dev.vinpol.spacetraders.sdk.models.Chart;
import dev.vinpol.spacetraders.sdk.models.WaypointFaction;
import dev.vinpol.spacetraders.sdk.models.WaypointOrbital;
import dev.vinpol.spacetraders.sdk.models.WaypointTrait;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A waypoint that was scanned by a ship.
 */

public class ScannedWaypoint {
  public static final String SERIALIZED_NAME_SYMBOL = "symbol";

  private String symbol;

  public static final String SERIALIZED_NAME_TYPE = "type";

  private WaypointType type;

  public static final String SERIALIZED_NAME_SYSTEM_SYMBOL = "systemSymbol";

  private String systemSymbol;

  public static final String SERIALIZED_NAME_X = "x";

  private Integer x;

  public static final String SERIALIZED_NAME_Y = "y";

  private Integer y;

  public static final String SERIALIZED_NAME_ORBITALS = "orbitals";

  private List<WaypointOrbital> orbitals = new ArrayList<>();

  public static final String SERIALIZED_NAME_FACTION = "faction";

  private WaypointFaction faction;

  public static final String SERIALIZED_NAME_TRAITS = "traits";

  private List<WaypointTrait> traits = new ArrayList<>();

  public static final String SERIALIZED_NAME_CHART = "chart";

  private Chart chart;

  public ScannedWaypoint() {
  }

  public ScannedWaypoint symbol(String symbol) {

    this.symbol = symbol;
    return this;
  }

   /**
   * The symbol of the waypoint.
   * @return symbol
  **/


  public String getSymbol() {
    return symbol;
  }


  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }


  public ScannedWaypoint type(WaypointType type) {

    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/


  public WaypointType getType() {
    return type;
  }


  public void setType(WaypointType type) {
    this.type = type;
  }


  public ScannedWaypoint systemSymbol(String systemSymbol) {

    this.systemSymbol = systemSymbol;
    return this;
  }

   /**
   * The symbol of the system.
   * @return systemSymbol
  **/


  public String getSystemSymbol() {
    return systemSymbol;
  }


  public void setSystemSymbol(String systemSymbol) {
    this.systemSymbol = systemSymbol;
  }


  public ScannedWaypoint x(Integer x) {

    this.x = x;
    return this;
  }

   /**
   * Position in the universe in the x axis.
   * @return x
  **/


  public Integer getX() {
    return x;
  }


  public void setX(Integer x) {
    this.x = x;
  }


  public ScannedWaypoint y(Integer y) {

    this.y = y;
    return this;
  }

   /**
   * Position in the universe in the y axis.
   * @return y
  **/


  public Integer getY() {
    return y;
  }


  public void setY(Integer y) {
    this.y = y;
  }


  public ScannedWaypoint orbitals(List<WaypointOrbital> orbitals) {

    this.orbitals = orbitals;
    return this;
  }

  public ScannedWaypoint addOrbitalsItem(WaypointOrbital orbitalsItem) {
    if (this.orbitals == null) {
      this.orbitals = new ArrayList<>();
    }
    this.orbitals.add(orbitalsItem);
    return this;
  }

   /**
   * List of waypoints that orbit this waypoint.
   * @return orbitals
  **/


  public List<WaypointOrbital> getOrbitals() {
    return orbitals;
  }


  public void setOrbitals(List<WaypointOrbital> orbitals) {
    this.orbitals = orbitals;
  }


  public ScannedWaypoint faction(WaypointFaction faction) {

    this.faction = faction;
    return this;
  }

   /**
   * Get faction
   * @return faction
  **/
  

  public WaypointFaction getFaction() {
    return faction;
  }


  public void setFaction(WaypointFaction faction) {
    this.faction = faction;
  }


  public ScannedWaypoint traits(List<WaypointTrait> traits) {

    this.traits = traits;
    return this;
  }

  public ScannedWaypoint addTraitsItem(WaypointTrait traitsItem) {
    if (this.traits == null) {
      this.traits = new ArrayList<>();
    }
    this.traits.add(traitsItem);
    return this;
  }

   /**
   * The traits of the waypoint.
   * @return traits
  **/


  public List<WaypointTrait> getTraits() {
    return traits;
  }


  public void setTraits(List<WaypointTrait> traits) {
    this.traits = traits;
  }


  public ScannedWaypoint chart(Chart chart) {

    this.chart = chart;
    return this;
  }

   /**
   * Get chart
   * @return chart
  **/
  

  public Chart getChart() {
    return chart;
  }


  public void setChart(Chart chart) {
    this.chart = chart;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScannedWaypoint scannedWaypoint = (ScannedWaypoint) o;
    return Objects.equals(this.symbol, scannedWaypoint.symbol) &&
        Objects.equals(this.type, scannedWaypoint.type) &&
        Objects.equals(this.systemSymbol, scannedWaypoint.systemSymbol) &&
        Objects.equals(this.x, scannedWaypoint.x) &&
        Objects.equals(this.y, scannedWaypoint.y) &&
        Objects.equals(this.orbitals, scannedWaypoint.orbitals) &&
        Objects.equals(this.faction, scannedWaypoint.faction) &&
        Objects.equals(this.traits, scannedWaypoint.traits) &&
        Objects.equals(this.chart, scannedWaypoint.chart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, type, systemSymbol, x, y, orbitals, faction, traits, chart);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScannedWaypoint {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    systemSymbol: ").append(toIndentedString(systemSymbol)).append("\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
    sb.append("    orbitals: ").append(toIndentedString(orbitals)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    traits: ").append(toIndentedString(traits)).append("\n");
    sb.append("    chart: ").append(toIndentedString(chart)).append("\n");
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

