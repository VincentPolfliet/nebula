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


import dev.vinpol.spacetraders.sdk.models.Agent;
import dev.vinpol.spacetraders.sdk.models.Contract;
import dev.vinpol.spacetraders.sdk.models.Faction;
import dev.vinpol.spacetraders.sdk.models.Ship;
import java.io.IOException;

/**
 * Register201ResponseData
 */

public class Register201ResponseData {
  public static final String SERIALIZED_NAME_AGENT = "agent";
  
  private Agent agent;

  public static final String SERIALIZED_NAME_CONTRACT = "contract";
  
  private Contract contract;

  public static final String SERIALIZED_NAME_FACTION = "faction";
  
  private Faction faction;

  public static final String SERIALIZED_NAME_SHIP = "ship";
  
  private Ship ship;

  public static final String SERIALIZED_NAME_TOKEN = "token";
  
  private String token;

  public Register201ResponseData() {
  }

  public Register201ResponseData agent(Agent agent) {

    this.agent = agent;
    return this;
  }

   /**
   * Get agent
   * @return agent
  **/
  

  public Agent getAgent() {
    return agent;
  }


  public void setAgent(Agent agent) {
    this.agent = agent;
  }


  public Register201ResponseData contract(Contract contract) {

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


  public Register201ResponseData faction(Faction faction) {

    this.faction = faction;
    return this;
  }

   /**
   * Get faction
   * @return faction
  **/
  

  public Faction getFaction() {
    return faction;
  }


  public void setFaction(Faction faction) {
    this.faction = faction;
  }


  public Register201ResponseData ship(Ship ship) {

    this.ship = ship;
    return this;
  }

   /**
   * Get ship
   * @return ship
  **/
  

  public Ship getShip() {
    return ship;
  }


  public void setShip(Ship ship) {
    this.ship = ship;
  }


  public Register201ResponseData token(String token) {

    this.token = token;
    return this;
  }

   /**
   * A Bearer token for accessing secured API endpoints.
   * @return token
  **/
  

  public String getToken() {
    return token;
  }


  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Register201ResponseData register201ResponseData = (Register201ResponseData) o;
    return Objects.equals(this.agent, register201ResponseData.agent) &&
        Objects.equals(this.contract, register201ResponseData.contract) &&
        Objects.equals(this.faction, register201ResponseData.faction) &&
        Objects.equals(this.ship, register201ResponseData.ship) &&
        Objects.equals(this.token, register201ResponseData.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agent, contract, faction, ship, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Register201ResponseData {\n");
    sb.append("    agent: ").append(toIndentedString(agent)).append("\n");
    sb.append("    contract: ").append(toIndentedString(contract)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    ship: ").append(toIndentedString(ship)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

