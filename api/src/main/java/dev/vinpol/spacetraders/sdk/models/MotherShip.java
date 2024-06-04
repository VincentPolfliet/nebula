package dev.vinpol.spacetraders.sdk.models;

import java.util.concurrent.ThreadLocalRandom;

public class MotherShip {

    private MotherShip() {

    }

    public static Ship satellite() {
        String shipSymbol = ShipSymbolGenerator.generate();

        return new Ship()
            .symbol(shipSymbol)
            .engine(
                new ShipEngine()
                    .speed(100)
            )
            .nav(new ShipNav()
                .route(new ShipNavRoute())
                .status(ShipNavStatus.IN_ORBIT)
            )
            .registration(
                new ShipRegistration()
                    .role(ShipRole.SATELLITE)
                    .factionSymbol(randomFaction().getValue())
                    .name(shipSymbol)
            )
            .cargo(
                new ShipCargo()
                    .units(0)
                    .capacity(0)
            )
            .cooldown(Cooldown.noCooldown(shipSymbol))
            .fuel(ShipFuel.infinite());
    }

    public static Ship excavator() {
        String shipSymbol = ShipSymbolGenerator.generate();

        return new Ship()
            .symbol(shipSymbol)
            .engine(
                new ShipEngine()
                    .speed(100)
            )
            .nav(new ShipNav()
                .route(new ShipNavRoute())
                .status(ShipNavStatus.IN_ORBIT)
            )
            .registration(
                new ShipRegistration()
                    .role(ShipRole.EXCAVATOR)
                    .factionSymbol(randomFaction().getValue())
                    .name(shipSymbol)
            )
            .cargo(
                new ShipCargo()
                    .units(0)
                    .capacity(16)
            )
            .cooldown(Cooldown.noCooldown(shipSymbol))
            .fuel(
                new ShipFuel()
                    .capacity(100)
                    .current(100)
            );
    }

    private static FactionSymbol randomFaction() {
        FactionSymbol[] factions = FactionSymbol.values();
        return factions[ThreadLocalRandom.current().nextInt(factions.length)];
    }

}
