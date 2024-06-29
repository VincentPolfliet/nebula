package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

@Data
public class ShipEvent {
    private String name;
    private String symbol;
    private String component;
    private String description;
    private String type;
}
