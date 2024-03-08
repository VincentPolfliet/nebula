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


import dev.vinpol.spacetraders.sdk.models.ShipType;
import java.io.IOException;

/**
 * PurchaseShipRequest
 */

public class PurchaseShipRequest {
  public static final String SERIALIZED_NAME_SHIP_TYPE = "shipType";
  
  private ShipType shipType;

  public static final String SERIALIZED_NAME_WAYPOINT_SYMBOL = "waypointSymbol";
  
  private String waypointSymbol;

  public PurchaseShipRequest() {
  }

  public PurchaseShipRequest shipType(ShipType shipType) {

    this.shipType = shipType;
    return this;
  }

   /**
   * Get shipType
   * @return shipType
  **/
  

  public ShipType getShipType() {
    return shipType;
  }


  public void setShipType(ShipType shipType) {
    this.shipType = shipType;
  }


  public PurchaseShipRequest waypointSymbol(String waypointSymbol) {

    this.waypointSymbol = waypointSymbol;
    return this;
  }

   /**
   * The symbol of the waypoint you want to purchase the ship at.
   * @return waypointSymbol
  **/
  

  public String getWaypointSymbol() {
    return waypointSymbol;
  }


  public void setWaypointSymbol(String waypointSymbol) {
    this.waypointSymbol = waypointSymbol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PurchaseShipRequest purchaseShipRequest = (PurchaseShipRequest) o;
    return Objects.equals(this.shipType, purchaseShipRequest.shipType) &&
        Objects.equals(this.waypointSymbol, purchaseShipRequest.waypointSymbol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipType, waypointSymbol);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PurchaseShipRequest {\n");
    sb.append("    shipType: ").append(toIndentedString(shipType)).append("\n");
    sb.append("    waypointSymbol: ").append(toIndentedString(waypointSymbol)).append("\n");
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
