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
 * The details of a delivery contract. Includes the type of good, units needed, and the destination.
 */

public class ContractDeliverGood {
  public static final String SERIALIZED_NAME_TRADE_SYMBOL = "tradeSymbol";
  
  private String tradeSymbol;

  public static final String SERIALIZED_NAME_DESTINATION_SYMBOL = "destinationSymbol";
  
  private String destinationSymbol;

  public static final String SERIALIZED_NAME_UNITS_REQUIRED = "unitsRequired";
  
  private Integer unitsRequired;

  public static final String SERIALIZED_NAME_UNITS_FULFILLED = "unitsFulfilled";
  
  private Integer unitsFulfilled;

  public ContractDeliverGood() {
  }

  public ContractDeliverGood tradeSymbol(String tradeSymbol) {
    
    this.tradeSymbol = tradeSymbol;
    return this;
  }

   /**
   * The symbol of the trade good to deliver.
   * @return tradeSymbol
  **/
  

  public String getTradeSymbol() {
    return tradeSymbol;
  }


  public void setTradeSymbol(String tradeSymbol) {
    this.tradeSymbol = tradeSymbol;
  }


  public ContractDeliverGood destinationSymbol(String destinationSymbol) {
    
    this.destinationSymbol = destinationSymbol;
    return this;
  }

   /**
   * The destination where goods need to be delivered.
   * @return destinationSymbol
  **/
  

  public String getDestinationSymbol() {
    return destinationSymbol;
  }


  public void setDestinationSymbol(String destinationSymbol) {
    this.destinationSymbol = destinationSymbol;
  }


  public ContractDeliverGood unitsRequired(Integer unitsRequired) {
    
    this.unitsRequired = unitsRequired;
    return this;
  }

   /**
   * The number of units that need to be delivered on this contract.
   * @return unitsRequired
  **/
  

  public Integer getUnitsRequired() {
    return unitsRequired;
  }


  public void setUnitsRequired(Integer unitsRequired) {
    this.unitsRequired = unitsRequired;
  }


  public ContractDeliverGood unitsFulfilled(Integer unitsFulfilled) {
    
    this.unitsFulfilled = unitsFulfilled;
    return this;
  }

   /**
   * The number of units fulfilled on this contract.
   * @return unitsFulfilled
  **/
  

  public Integer getUnitsFulfilled() {
    return unitsFulfilled;
  }


  public void setUnitsFulfilled(Integer unitsFulfilled) {
    this.unitsFulfilled = unitsFulfilled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractDeliverGood contractDeliverGood = (ContractDeliverGood) o;
    return Objects.equals(this.tradeSymbol, contractDeliverGood.tradeSymbol) &&
        Objects.equals(this.destinationSymbol, contractDeliverGood.destinationSymbol) &&
        Objects.equals(this.unitsRequired, contractDeliverGood.unitsRequired) &&
        Objects.equals(this.unitsFulfilled, contractDeliverGood.unitsFulfilled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradeSymbol, destinationSymbol, unitsRequired, unitsFulfilled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractDeliverGood {\n");
    sb.append("    tradeSymbol: ").append(toIndentedString(tradeSymbol)).append("\n");
    sb.append("    destinationSymbol: ").append(toIndentedString(destinationSymbol)).append("\n");
    sb.append("    unitsRequired: ").append(toIndentedString(unitsRequired)).append("\n");
    sb.append("    unitsFulfilled: ").append(toIndentedString(unitsFulfilled)).append("\n");
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

