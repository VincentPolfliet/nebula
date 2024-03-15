package dev.vinpol.spacetraders.sdk.models;

import java.util.concurrent.ThreadLocalRandom;

public class ShipMother {

    private ShipMother() {

    }

    public static Ship excavator() {
        String shipSymbol = ShipSymbolGenerator.generate();

        return new Ship()
            .symbol(shipSymbol)
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
            .cooldown(
                new Cooldown()
                    .remainingSeconds(0)
                    .totalSeconds(0)
                    .expiration(null)
            ).fuel(
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
