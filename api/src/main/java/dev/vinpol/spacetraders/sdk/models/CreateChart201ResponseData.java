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
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import java.io.IOException;

/**
 * CreateChart201ResponseData
 */

public class CreateChart201ResponseData {
  public static final String SERIALIZED_NAME_CHART = "chart";
  
  private Chart chart;

  public static final String SERIALIZED_NAME_WAYPOINT = "waypoint";
  
  private Waypoint waypoint;

  public CreateChart201ResponseData() {
  }

  public CreateChart201ResponseData chart(Chart chart) {

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


  public CreateChart201ResponseData waypoint(Waypoint waypoint) {

    this.waypoint = waypoint;
    return this;
  }

   /**
   * Get waypoint
   * @return waypoint
  **/
  

  public Waypoint getWaypoint() {
    return waypoint;
  }


  public void setWaypoint(Waypoint waypoint) {
    this.waypoint = waypoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateChart201ResponseData createChart201ResponseData = (CreateChart201ResponseData) o;
    return Objects.equals(this.chart, createChart201ResponseData.chart) &&
        Objects.equals(this.waypoint, createChart201ResponseData.waypoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chart, waypoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateChart201ResponseData {\n");
    sb.append("    chart: ").append(toIndentedString(chart)).append("\n");
    sb.append("    waypoint: ").append(toIndentedString(waypoint)).append("\n");
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

