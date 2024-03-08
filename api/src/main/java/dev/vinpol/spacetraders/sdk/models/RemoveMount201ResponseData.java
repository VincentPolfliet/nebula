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
import dev.vinpol.spacetraders.sdk.models.ShipCargo;
import dev.vinpol.spacetraders.sdk.models.ShipModificationTransaction;
import dev.vinpol.spacetraders.sdk.models.ShipMount;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RemoveMount201ResponseData
 */

public class RemoveMount201ResponseData {
  public static final String SERIALIZED_NAME_AGENT = "agent";

  private Agent agent;

  public static final String SERIALIZED_NAME_MOUNTS = "mounts";

  private List<ShipMount> mounts = new ArrayList<>();

  public static final String SERIALIZED_NAME_CARGO = "cargo";

  private ShipCargo cargo;

  public static final String SERIALIZED_NAME_TRANSACTION = "transaction";

  private ShipModificationTransaction transaction;

  public RemoveMount201ResponseData() {
  }

  public RemoveMount201ResponseData agent(Agent agent) {

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


  public RemoveMount201ResponseData mounts(List<ShipMount> mounts) {

    this.mounts = mounts;
    return this;
  }

  public RemoveMount201ResponseData addMountsItem(ShipMount mountsItem) {
    if (this.mounts == null) {
      this.mounts = new ArrayList<>();
    }
    this.mounts.add(mountsItem);
    return this;
  }

   /**
   * List of installed mounts after the removal of the selected mount.
   * @return mounts
  **/


  public List<ShipMount> getMounts() {
    return mounts;
  }


  public void setMounts(List<ShipMount> mounts) {
    this.mounts = mounts;
  }


  public RemoveMount201ResponseData cargo(ShipCargo cargo) {

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


  public RemoveMount201ResponseData transaction(ShipModificationTransaction transaction) {

    this.transaction = transaction;
    return this;
  }

   /**
   * Get transaction
   * @return transaction
  **/


  public ShipModificationTransaction getTransaction() {
    return transaction;
  }


  public void setTransaction(ShipModificationTransaction transaction) {
    this.transaction = transaction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RemoveMount201ResponseData removeMount201ResponseData = (RemoveMount201ResponseData) o;
    return Objects.equals(this.agent, removeMount201ResponseData.agent) &&
        Objects.equals(this.mounts, removeMount201ResponseData.mounts) &&
        Objects.equals(this.cargo, removeMount201ResponseData.cargo) &&
        Objects.equals(this.transaction, removeMount201ResponseData.transaction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agent, mounts, cargo, transaction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RemoveMount201ResponseData {\n");
    sb.append("    agent: ").append(toIndentedString(agent)).append("\n");
    sb.append("    mounts: ").append(toIndentedString(mounts)).append("\n");
    sb.append("    cargo: ").append(toIndentedString(cargo)).append("\n");
    sb.append("    transaction: ").append(toIndentedString(transaction)).append("\n");
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

