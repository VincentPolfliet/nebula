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


import dev.vinpol.spacetraders.sdk.models.SystemFaction;
import dev.vinpol.spacetraders.sdk.models.SystemType;
import dev.vinpol.spacetraders.sdk.models.SystemWaypoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * System
 */

public class System {
  public static final String SERIALIZED_NAME_SYMBOL = "symbol";

  private String symbol;

  public static final String SERIALIZED_NAME_SECTOR_SYMBOL = "sectorSymbol";
  
  private String sectorSymbol;

  public static final String SERIALIZED_NAME_TYPE = "type";
  
  private SystemType type;

  public static final String SERIALIZED_NAME_X = "x";
  
  private Integer x;

  public static final String SERIALIZED_NAME_Y = "y";
  
  private Integer y;

  public static final String SERIALIZED_NAME_WAYPOINTS = "waypoints";
  
  private List<SystemWaypoint> waypoints = new ArrayList<>();

  public static final String SERIALIZED_NAME_FACTIONS = "factions";
  
  private List<SystemFaction> factions = new ArrayList<>();

  public System() {
  }

  public System symbol(String symbol) {

    this.symbol = symbol;
    return this;
  }

   /**
   * The symbol of the system.
   * @return symbol
  **/
  

  public String getSymbol() {
    return symbol;
  }


  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }


  public System sectorSymbol(String sectorSymbol) {

    this.sectorSymbol = sectorSymbol;
    return this;
  }

   /**
   * The symbol of the sector.
   * @return sectorSymbol
  **/
  

  public String getSectorSymbol() {
    return sectorSymbol;
  }


  public void setSectorSymbol(String sectorSymbol) {
    this.sectorSymbol = sectorSymbol;
  }


  public System type(SystemType type) {

    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  

  public SystemType getType() {
    return type;
  }


  public void setType(SystemType type) {
    this.type = type;
  }


  public System x(Integer x) {

    this.x = x;
    return this;
  }

   /**
   * Relative position of the system in the sector in the x axis.
   * @return x
  **/
  

  public Integer getX() {
    return x;
  }


  public void setX(Integer x) {
    this.x = x;
  }


  public System y(Integer y) {

    this.y = y;
    return this;
  }

   /**
   * Relative position of the system in the sector in the y axis.
   * @return y
  **/
  

  public Integer getY() {
    return y;
  }


  public void setY(Integer y) {
    this.y = y;
  }


  public System waypoints(List<SystemWaypoint> waypoints) {

    this.waypoints = waypoints;
    return this;
  }

  public System addWaypointsItem(SystemWaypoint waypointsItem) {
    if (this.waypoints == null) {
      this.waypoints = new ArrayList<>();
    }
    this.waypoints.add(waypointsItem);
    return this;
  }

   /**
   * Waypoints in this system.
   * @return waypoints
  **/
  

  public List<SystemWaypoint> getWaypoints() {
    return waypoints;
  }


  public void setWaypoints(List<SystemWaypoint> waypoints) {
    this.waypoints = waypoints;
  }


  public System factions(List<SystemFaction> factions) {

    this.factions = factions;
    return this;
  }

  public System addFactionsItem(SystemFaction factionsItem) {
    if (this.factions == null) {
      this.factions = new ArrayList<>();
    }
    this.factions.add(factionsItem);
    return this;
  }

   /**
   * Factions that control this system.
   * @return factions
  **/
  

  public List<SystemFaction> getFactions() {
    return factions;
  }


  public void setFactions(List<SystemFaction> factions) {
    this.factions = factions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    System system = (System) o;
    return Objects.equals(this.symbol, system.symbol) &&
        Objects.equals(this.sectorSymbol, system.sectorSymbol) &&
        Objects.equals(this.type, system.type) &&
        Objects.equals(this.x, system.x) &&
        Objects.equals(this.y, system.y) &&
        Objects.equals(this.waypoints, system.waypoints) &&
        Objects.equals(this.factions, system.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, sectorSymbol, type, x, y, waypoints, factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class System {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    sectorSymbol: ").append(toIndentedString(sectorSymbol)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    x: ").append(toIndentedString(x)).append("\n");
    sb.append("    y: ").append(toIndentedString(y)).append("\n");
    sb.append("    waypoints: ").append(toIndentedString(waypoints)).append("\n");
    sb.append("    factions: ").append(toIndentedString(factions)).append("\n");
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
