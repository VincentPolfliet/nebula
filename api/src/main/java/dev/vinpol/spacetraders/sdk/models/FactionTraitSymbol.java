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
 * The unique identifier of the trait.
 */

public enum FactionTraitSymbol {

  BUREAUCRATIC("BUREAUCRATIC"),

  SECRETIVE("SECRETIVE"),

  CAPITALISTIC("CAPITALISTIC"),

  INDUSTRIOUS("INDUSTRIOUS"),

  PEACEFUL("PEACEFUL"),

  DISTRUSTFUL("DISTRUSTFUL"),

  WELCOMING("WELCOMING"),

  SMUGGLERS("SMUGGLERS"),

  SCAVENGERS("SCAVENGERS"),

  REBELLIOUS("REBELLIOUS"),

  EXILES("EXILES"),

  PIRATES("PIRATES"),

  RAIDERS("RAIDERS"),

  CLAN("CLAN"),

  GUILD("GUILD"),

  DOMINION("DOMINION"),

  FRINGE("FRINGE"),

  FORSAKEN("FORSAKEN"),

  ISOLATED("ISOLATED"),

  LOCALIZED("LOCALIZED"),

  ESTABLISHED("ESTABLISHED"),

  NOTABLE("NOTABLE"),

  DOMINANT("DOMINANT"),

  INESCAPABLE("INESCAPABLE"),

  INNOVATIVE("INNOVATIVE"),

  BOLD("BOLD"),

  VISIONARY("VISIONARY"),

  CURIOUS("CURIOUS"),

  DARING("DARING"),

  EXPLORATORY("EXPLORATORY"),

  RESOURCEFUL("RESOURCEFUL"),

  FLEXIBLE("FLEXIBLE"),

  COOPERATIVE("COOPERATIVE"),

  UNITED("UNITED"),

  STRATEGIC("STRATEGIC"),

  INTELLIGENT("INTELLIGENT"),

  RESEARCH_FOCUSED("RESEARCH_FOCUSED"),

  COLLABORATIVE("COLLABORATIVE"),

  PROGRESSIVE("PROGRESSIVE"),

  MILITARISTIC("MILITARISTIC"),

  TECHNOLOGICALLY_ADVANCED("TECHNOLOGICALLY_ADVANCED"),

  AGGRESSIVE("AGGRESSIVE"),

  IMPERIALISTIC("IMPERIALISTIC"),

  TREASURE_HUNTERS("TREASURE_HUNTERS"),

  DEXTEROUS("DEXTEROUS"),

  UNPREDICTABLE("UNPREDICTABLE"),

  BRUTAL("BRUTAL"),

  FLEETING("FLEETING"),

  ADAPTABLE("ADAPTABLE"),

  SELF_SUFFICIENT("SELF_SUFFICIENT"),

  DEFENSIVE("DEFENSIVE"),

  PROUD("PROUD"),

  DIVERSE("DIVERSE"),

  INDEPENDENT("INDEPENDENT"),

  SELF_INTERESTED("SELF_INTERESTED"),

  FRAGMENTED("FRAGMENTED"),

  COMMERCIAL("COMMERCIAL"),

  FREE_MARKETS("FREE_MARKETS"),

  ENTREPRENEURIAL("ENTREPRENEURIAL");

  private String value;

  FactionTraitSymbol(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static FactionTraitSymbol fromValue(String value) {
    for (FactionTraitSymbol b : FactionTraitSymbol.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

