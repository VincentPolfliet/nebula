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


import java.io.IOException;

/**
 * GetStatus200ResponseStats
 */

public class GetStatus200ResponseStats {
  public static final String SERIALIZED_NAME_AGENTS = "agents";

  private Integer agents;

  public static final String SERIALIZED_NAME_SHIPS = "ships";

  private Integer ships;

  public static final String SERIALIZED_NAME_SYSTEMS = "systems";

  private Integer systems;

  public static final String SERIALIZED_NAME_WAYPOINTS = "waypoints";

  private Integer waypoints;

  public GetStatus200ResponseStats() {
  }

  public GetStatus200ResponseStats agents(Integer agents) {

    this.agents = agents;
    return this;
  }

   /**
   * Number of registered agents in the game.
   * @return agents
  **/


  public Integer getAgents() {
    return agents;
  }


  public void setAgents(Integer agents) {
    this.agents = agents;
  }


  public GetStatus200ResponseStats ships(Integer ships) {

    this.ships = ships;
    return this;
  }

   /**
   * Total number of ships in the game.
   * @return ships
  **/


  public Integer getShips() {
    return ships;
  }


  public void setShips(Integer ships) {
    this.ships = ships;
  }


  public GetStatus200ResponseStats systems(Integer systems) {

    this.systems = systems;
    return this;
  }

   /**
   * Total number of systems in the game.
   * @return systems
  **/


  public Integer getSystems() {
    return systems;
  }


  public void setSystems(Integer systems) {
    this.systems = systems;
  }


  public GetStatus200ResponseStats waypoints(Integer waypoints) {

    this.waypoints = waypoints;
    return this;
  }

   /**
   * Total number of waypoints in the game.
   * @return waypoints
  **/


  public Integer getWaypoints() {
    return waypoints;
  }


  public void setWaypoints(Integer waypoints) {
    this.waypoints = waypoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStatus200ResponseStats getStatus200ResponseStats = (GetStatus200ResponseStats) o;
    return Objects.equals(this.agents, getStatus200ResponseStats.agents) &&
        Objects.equals(this.ships, getStatus200ResponseStats.ships) &&
        Objects.equals(this.systems, getStatus200ResponseStats.systems) &&
        Objects.equals(this.waypoints, getStatus200ResponseStats.waypoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agents, ships, systems, waypoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStatus200ResponseStats {\n");
    sb.append("    agents: ").append(toIndentedString(agents)).append("\n");
    sb.append("    ships: ").append(toIndentedString(ships)).append("\n");
    sb.append("    systems: ").append(toIndentedString(systems)).append("\n");
    sb.append("    waypoints: ").append(toIndentedString(waypoints)).append("\n");
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

