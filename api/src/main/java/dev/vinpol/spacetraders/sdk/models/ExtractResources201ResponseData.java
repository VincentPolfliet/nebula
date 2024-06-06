package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.List;

@Data
public class ExtractResources201ResponseData {
    private Cooldown cooldown;
    private Extraction extraction;
    private ShipCargo cargo;
    private List<ShipEvent> events;

    public ExtractResources201ResponseData cooldown(Cooldown cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public ExtractResources201ResponseData extraction(Extraction extraction) {
        this.extraction = extraction;
        return this;
    }

    public ExtractResources201ResponseData cargo(ShipCargo cargo) {
        this.cargo = cargo;
        return this;
    }
}

