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
 * ShipRefineRequest
 */

public class ShipRefineRequest {
  /**
   * The type of good to produce out of the refining process.
   */

  public enum ProduceEnum {
    IRON("IRON"),

    COPPER("COPPER"),

    SILVER("SILVER"),

    GOLD("GOLD"),

    ALUMINUM("ALUMINUM"),

    PLATINUM("PLATINUM"),

    URANITE("URANITE"),

    MERITIUM("MERITIUM"),

    FUEL("FUEL");

    private String value;

    ProduceEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static ProduceEnum fromValue(String value) {
      for (ProduceEnum b : ProduceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String SERIALIZED_NAME_PRODUCE = "produce";

  private ProduceEnum produce;

  public ShipRefineRequest() {
  }

  public ShipRefineRequest produce(ProduceEnum produce) {

    this.produce = produce;
    return this;
  }

   /**
   * The type of good to produce out of the refining process.
   * @return produce
  **/


  public ProduceEnum getProduce() {
    return produce;
  }


  public void setProduce(ProduceEnum produce) {
    this.produce = produce;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShipRefineRequest shipRefineRequest = (ShipRefineRequest) o;
    return Objects.equals(this.produce, shipRefineRequest.produce);
  }

  @Override
  public int hashCode() {
    return Objects.hash(produce);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShipRefineRequest {\n");
    sb.append("    produce: ").append(toIndentedString(produce)).append("\n");
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

