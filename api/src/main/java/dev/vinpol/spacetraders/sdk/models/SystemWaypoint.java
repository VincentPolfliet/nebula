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


import dev.vinpol.spacetraders.sdk.models.WaypointOrbital;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * SystemWaypoint
 */

public class SystemWaypoint {
  public static final String SERIALIZED_NAME_SYMBOL = "symbol";

  private String symbol;

  public static final String SERIALIZED_NAME_TYPE = "type";

  private WaypointType type;

  public static final String SERIALIZED_NAME_X = "x";

  private Integer x;

  public static final String SERIALIZED_NAME_Y = "y";

  private Integer y;

  public static final String SERIALIZED_NAME_ORBITALS = "orbitals";

  private List<WaypointOrbital> orbitals = new ArrayList<>();

  public static final String SERIALIZED_NAME_ORBITS = "orbits";

  private String orbits;

  public SystemWaypoint() {
  }

  public SystemWaypoint symbol(String symbol) {

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


  public SystemWaypoint type(WaypointType type) {

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


  public SystemWaypoint x(Integer x) {

    this.x = x;
    return this;
  }

   /**
   * Relative position of the waypoint on the system&#39;s x axis. This is not an absolute position in the universe.
   * @return x
  **/


  public Integer getX() {
    return x;
  }


  public void setX(Integer x) {
    this.x = x;
  }


  public SystemWaypoint y(Integer y) {

    this.y = y;
    return this;
  }

   /**
   * Relative position of the waypoint on the system&#39;s y axis. This is not an absolute position in the universe.
   * @return y
  **/


  public Integer getY() {
    return y;
  }


  public void setY(Integer y) {
    this.y = y;
  }


  public SystemWaypoint orbitals(List<WaypointOrbital> orbitals) {

    this.orbitals = orbitals;
    return this;
  }

  public SystemWaypoint addOrbitalsItem(WaypointOrbital orbitalsItem) {
    if (this.orbitals == null) {
      this.orbitals = new ArrayList<>();
    }
    this.orbitals.add(orbitalsItem);
    return this;
  }

   /**
   * Waypoints that orbit this waypoint.
   * @return orbitals
  **/


  public List<WaypointOrbital> getOrbitals() {
    return orbitals;
  }


  public void setOrbitals(List<WaypointOrbital> orbitals) {
    this.orbitals = orbitals;
  }


  public SystemWaypoint orbits(String orbits) {

    this.orbits = orbits;
    return this;
  }

   /**
   * The symbol of the parent waypoint, if this waypoint is in orbit around another waypoint. Otherwise this value is undefined.
   * @return orbits
  **/
  

  public String getOrbits() {
    return orbits;
  }


  public void setOrbits(String orbits) {
    this.orbits = orbits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemWaypoint systemWaypoint = (SystemWaypoint) o;
    return Objects.equals(this.symbol, systemWaypoint.symbol) &&
        Objects.equals(this.type, systemWaypoint.type) &&
        Objects.equals(this.x, systemWaypoint.x) &&
        Objects.equals(this.y, systemWaypoint.y) &&
        Objects.equals(this.orbitals, systemWaypoint.orbitals) &&
        Objects.equals(this.orbits, systemWaypoint.orbits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, type, x, y, orbitals, orbits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemWaypoint {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
    sb.append("    orbitals: ").append(toIndentedString(orbitals)).append("\n");
    sb.append("    orbits: ").append(toIndentedString(orbits)).append("\n");
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
