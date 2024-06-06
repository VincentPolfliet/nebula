package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.spacetraders.sdk.models.Market;
import dev.vinpol.spacetraders.sdk.models.Waypoint;

public record MarketWaypoint(Waypoint waypoint, Market market, int canSellScore) {

}
