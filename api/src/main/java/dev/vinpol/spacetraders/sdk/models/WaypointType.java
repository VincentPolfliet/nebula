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

/**
 * The type of waypoint.
 */
public enum WaypointType {

    PLANET("PLANET"),

    GAS_GIANT("GAS_GIANT"),

    MOON("MOON"),

    ORBITAL_STATION("ORBITAL_STATION"),

    JUMP_GATE("JUMP_GATE"),

    ASTEROID_FIELD("ASTEROID_FIELD"),

    ASTEROID("ASTEROID"),

    ENGINEERED_ASTEROID("ENGINEERED_ASTEROID"),

    ASTEROID_BASE("ASTEROID_BASE"),

    NEBULA("NEBULA"),

    DEBRIS_FIELD("DEBRIS_FIELD"),

    GRAVITY_WELL("GRAVITY_WELL"),

    ARTIFICIAL_GRAVITY_WELL("ARTIFICIAL_GRAVITY_WELL"),

    FUEL_STATION("FUEL_STATION");

    private final String value;

    WaypointType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static WaypointType fromValue(String value) {
        for (WaypointType b : WaypointType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

