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


import dev.vinpol.spacetraders.sdk.models.Contract;
import dev.vinpol.spacetraders.sdk.models.ShipCargo;
import java.io.IOException;

/**
 * DeliverContract200ResponseData
 */

public class DeliverContract200ResponseData {
  public static final String SERIALIZED_NAME_CONTRACT = "contract";

  private Contract contract;

  public static final String SERIALIZED_NAME_CARGO = "cargo";

  private ShipCargo cargo;

  public DeliverContract200ResponseData() {
  }

  public DeliverContract200ResponseData contract(Contract contract) {

    this.contract = contract;
    return this;
  }

   /**
   * Get contract
   * @return contract
  **/


  public Contract getContract() {
    return contract;
  }


  public void setContract(Contract contract) {
    this.contract = contract;
  }


  public DeliverContract200ResponseData cargo(ShipCargo cargo) {

    this.cargo = cargo;
    return this;
  }

   /**
   * Get cargo
   * @return cargo
  **/


  public ShipCargo getCargo() {
    return cargo;
  }


  public void setCargo(ShipCargo cargo) {
    this.cargo = cargo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeliverContract200ResponseData deliverContract200ResponseData = (DeliverContract200ResponseData) o;
    return Objects.equals(this.contract, deliverContract200ResponseData.contract) &&
        Objects.equals(this.cargo, deliverContract200ResponseData.cargo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contract, cargo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeliverContract200ResponseData {\n");
    sb.append("    contract: ").append(toIndentedString(contract)).append("\n");
    sb.append("    cargo: ").append(toIndentedString(cargo)).append("\n");
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
