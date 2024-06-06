package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.spacetraders.sdk.models.WaypointTrait;
import dev.vinpol.spacetraders.sdk.models.WaypointTraitSymbol;

public class WaypointTraits {
    private WaypointTraits() {

    }

    public static WaypointTrait market() {
        return new WaypointTrait()
            .symbol(WaypointTraitSymbol.MARKETPLACE);
    }
}
