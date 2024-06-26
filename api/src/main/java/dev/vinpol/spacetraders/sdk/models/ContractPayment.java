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
 * Payments for the contract.
 */

public class ContractPayment {
  public static final String SERIALIZED_NAME_ON_ACCEPTED = "onAccepted";
  
  private Integer onAccepted;

  public static final String SERIALIZED_NAME_ON_FULFILLED = "onFulfilled";
  
  private Integer onFulfilled;

  public ContractPayment() {
  }

  public ContractPayment onAccepted(Integer onAccepted) {
    
    this.onAccepted = onAccepted;
    return this;
  }

   /**
   * The amount of credits received up front for accepting the contract.
   * @return onAccepted
  **/
  

  public Integer getOnAccepted() {
    return onAccepted;
  }


  public void setOnAccepted(Integer onAccepted) {
    this.onAccepted = onAccepted;
  }


  public ContractPayment onFulfilled(Integer onFulfilled) {
    
    this.onFulfilled = onFulfilled;
    return this;
  }

   /**
   * The amount of credits received when the contract is fulfilled.
   * @return onFulfilled
  **/
  

  public Integer getOnFulfilled() {
    return onFulfilled;
  }


  public void setOnFulfilled(Integer onFulfilled) {
    this.onFulfilled = onFulfilled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractPayment contractPayment = (ContractPayment) o;
    return Objects.equals(this.onAccepted, contractPayment.onAccepted) &&
        Objects.equals(this.onFulfilled, contractPayment.onFulfilled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(onAccepted, onFulfilled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractPayment {\n");
    sb.append("    onAccepted: ").append(toIndentedString(onAccepted)).append("\n");
    sb.append("    onFulfilled: ").append(toIndentedString(onFulfilled)).append("\n");
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

