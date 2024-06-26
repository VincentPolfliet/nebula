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


import dev.vinpol.spacetraders.sdk.models.FactionSymbol;
import dev.vinpol.spacetraders.sdk.models.FactionTrait;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Faction details.
 */

public class Faction {
  public static final String SERIALIZED_NAME_SYMBOL = "symbol";

  private FactionSymbol symbol;

  public static final String SERIALIZED_NAME_NAME = "name";

  private String name;

  public static final String SERIALIZED_NAME_DESCRIPTION = "description";

  private String description;

  public static final String SERIALIZED_NAME_HEADQUARTERS = "headquarters";
  
  private String headquarters;

  public static final String SERIALIZED_NAME_TRAITS = "traits";
  
  private List<FactionTrait> traits = new ArrayList<>();

  public static final String SERIALIZED_NAME_IS_RECRUITING = "isRecruiting";
  
  private Boolean isRecruiting;

  public Faction() {
  }

  public Faction symbol(FactionSymbol symbol) {

    this.symbol = symbol;
    return this;
  }

   /**
   * Get symbol
   * @return symbol
  **/
  

  public FactionSymbol getSymbol() {
    return symbol;
  }


  public void setSymbol(FactionSymbol symbol) {
    this.symbol = symbol;
  }


  public Faction name(String name) {

    this.name = name;
    return this;
  }

   /**
   * Name of the faction.
   * @return name
  **/
  

  public String getName() {
    return name;
  }


  public void setName(String name) {
    this.name = name;
  }


  public Faction description(String description) {

    this.description = description;
    return this;
  }

   /**
   * Description of the faction.
   * @return description
  **/
  

  public String getDescription() {
    return description;
  }


  public void setDescription(String description) {
    this.description = description;
  }


  public Faction headquarters(String headquarters) {

    this.headquarters = headquarters;
    return this;
  }

   /**
   * The waypoint in which the faction&#39;s HQ is located in.
   * @return headquarters
  **/
  

  public String getHeadquarters() {
    return headquarters;
  }


  public void setHeadquarters(String headquarters) {
    this.headquarters = headquarters;
  }


  public Faction traits(List<FactionTrait> traits) {

    this.traits = traits;
    return this;
  }

  public Faction addTraitsItem(FactionTrait traitsItem) {
    if (this.traits == null) {
      this.traits = new ArrayList<>();
    }
    this.traits.add(traitsItem);
    return this;
  }

   /**
   * List of traits that define this faction.
   * @return traits
  **/
  

  public List<FactionTrait> getTraits() {
    return traits;
  }


  public void setTraits(List<FactionTrait> traits) {
    this.traits = traits;
  }


  public Faction isRecruiting(Boolean isRecruiting) {

    this.isRecruiting = isRecruiting;
    return this;
  }

   /**
   * Whether or not the faction is currently recruiting new agents.
   * @return isRecruiting
  **/
  

  public Boolean getIsRecruiting() {
    return isRecruiting;
  }


  public void setIsRecruiting(Boolean isRecruiting) {
    this.isRecruiting = isRecruiting;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Faction faction = (Faction) o;
    return Objects.equals(this.symbol, faction.symbol) &&
        Objects.equals(this.name, faction.name) &&
        Objects.equals(this.description, faction.description) &&
        Objects.equals(this.headquarters, faction.headquarters) &&
        Objects.equals(this.traits, faction.traits) &&
        Objects.equals(this.isRecruiting, faction.isRecruiting);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, name, description, headquarters, traits, isRecruiting);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Faction {\n");
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    headquarters: ").append(toIndentedString(headquarters)).append("\n");
    sb.append("    traits: ").append(toIndentedString(traits)).append("\n");
    sb.append("    isRecruiting: ").append(toIndentedString(isRecruiting)).append("\n");
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

