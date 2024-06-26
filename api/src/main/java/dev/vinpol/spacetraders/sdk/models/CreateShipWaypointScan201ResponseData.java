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


import dev.vinpol.spacetraders.sdk.models.Cooldown;
import dev.vinpol.spacetraders.sdk.models.ScannedWaypoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CreateShipWaypointScan201ResponseData
 */

public class CreateShipWaypointScan201ResponseData {
  public static final String SERIALIZED_NAME_COOLDOWN = "cooldown";

  private Cooldown cooldown;

  public static final String SERIALIZED_NAME_WAYPOINTS = "waypoints";

  private List<ScannedWaypoint> waypoints = new ArrayList<>();

  public CreateShipWaypointScan201ResponseData() {
  }

  public CreateShipWaypointScan201ResponseData cooldown(Cooldown cooldown) {

    this.cooldown = cooldown;
    return this;
  }

   /**
   * Get cooldown
   * @return cooldown
  **/


  public Cooldown getCooldown() {
    return cooldown;
  }


  public void setCooldown(Cooldown cooldown) {
    this.cooldown = cooldown;
  }


  public CreateShipWaypointScan201ResponseData waypoints(List<ScannedWaypoint> waypoints) {

    this.waypoints = waypoints;
    return this;
  }

  public CreateShipWaypointScan201ResponseData addWaypointsItem(ScannedWaypoint waypointsItem) {
    if (this.waypoints == null) {
      this.waypoints = new ArrayList<>();
    }
    this.waypoints.add(waypointsItem);
    return this;
  }

   /**
   * List of scanned waypoints.
   * @return waypoints
  **/


  public List<ScannedWaypoint> getWaypoints() {
    return waypoints;
  }


  public void setWaypoints(List<ScannedWaypoint> waypoints) {
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
    CreateShipWaypointScan201ResponseData createShipWaypointScan201ResponseData = (CreateShipWaypointScan201ResponseData) o;
    return Objects.equals(this.cooldown, createShipWaypointScan201ResponseData.cooldown) &&
        Objects.equals(this.waypoints, createShipWaypointScan201ResponseData.waypoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cooldown, waypoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateShipWaypointScan201ResponseData {\n");
    sb.append("    cooldown: ").append(toIndentedString(cooldown)).append("\n");
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

