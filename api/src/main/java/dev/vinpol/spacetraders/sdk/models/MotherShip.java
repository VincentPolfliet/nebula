package dev.vinpol.spacetraders.sdk.models;

import java.util.concurrent.ThreadLocalRandom;

public class MotherShip {

    private MotherShip() {

    }

    public static Ship satellite() {
        String shipSymbol = ShipSymbolGenerator.generate();

        return new ShipBuilder()
            .withSymbol(shipSymbol)
            .withEngine(
                new ShipEngine()
                    .speed(100)
            )
            .withNav(new ShipNav()
                .route(new ShipNavRoute())
                .status(ShipNavStatus.IN_ORBIT)
            )
            .withRegistration(
                new ShipRegistration()
                    .role(ShipRole.SATELLITE)
                    .factionSymbol(randomFaction().getValue())
                    .name(shipSymbol)
            )
            .withCargo(
                new ShipCargo()
                    .units(0)
                    .capacity(0)
            )
            .withCooldown(Cooldown.noCooldown(shipSymbol))
            .withFuel(ShipFuel.infinite())
            .build();
    }

    public static Ship excavator() {
        String shipSymbol = ShipSymbolGenerator.generate();

        return new ShipBuilder()
            .withSymbol(shipSymbol)
            .withEngine(
                new ShipEngine()
                    .speed(100)
            )
            .withNav(new ShipNav()
                .route(new ShipNavRoute())
                .status(ShipNavStatus.IN_ORBIT)
            )
            .withRegistration(
                new ShipRegistration()
                    .role(ShipRole.EXCAVATOR)
                    .factionSymbol(randomFaction().getValue())
                    .name(shipSymbol)
            )
            .withCargo(
                new ShipCargo()
                    .units(0)
                    .capacity(16)
            )
            .withCooldown(Cooldown.noCooldown(shipSymbol))
            .withFuel(
                new ShipFuel()
                    .capacity(100)
                    .current(100)
            )
            .build();
    }

    private static FactionSymbol randomFaction() {
        FactionSymbol[] factions = FactionSymbol.values();
        return factions[ThreadLocalRandom.current().nextInt(factions.length)];
    }

}
