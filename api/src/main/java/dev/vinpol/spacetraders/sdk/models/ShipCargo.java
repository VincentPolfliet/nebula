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

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Ship cargo details.
 */

@Data
public class ShipCargo {
    /**
     * -- GETTER --
     *  The max number of items that can be stored in the cargo hold.
     *  minimum: 0
     *
     * @return capacity
     */
    @Getter
    private Integer capacity;
    /**
     * -- GETTER --
     *  The number of items currently stored in the cargo hold.
     *  minimum: 0
     *
     * @return units
     */
    @Getter
    private Integer units;
    private List<ShipCargoItem> inventory = new ArrayList<>();

    public ShipCargo capacity(Integer capacity) {

        this.capacity = capacity;
        return this;
    }

    public ShipCargo units(Integer units) {
        this.units = units;
        return this;
    }

    public ShipCargo inventory(List<ShipCargoItem> inventory) {
        this.inventory = inventory;
        return this;
    }

    public ShipCargo addInventoryItem(ShipCargoItem inventoryItem) {
        if (this.inventory == null) {
            this.inventory = new ArrayList<>();
        }

        this.inventory.add(inventoryItem);
        return this;
    }

    public boolean isFull() {
        return Objects.equals(capacity, units);
    }
}

