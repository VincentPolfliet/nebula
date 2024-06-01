package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourRefLeaf;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipBehaviourSequence;
import dev.vinpol.nebula.dragonship.automation.behaviour.tree.ShipTreeBehaviour;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.models.TradeSymbol;
import dev.vinpol.spacetraders.sdk.models.WaypointType;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public interface ShipBehaviourFactoryCreator {

    MiningBehaviourFactory miningAutomation(SystemSymbol system, WaypointType waypointType);

    ExtractionBehaviourFactory extraction();

    NavigateBehaviourFactory navigateAutomation(WaypointSymbol waypointSymbol);

    OrbitBehaviourFactory orbitAutomation();

    DockBehaviourFactory dock();

    RefuelBehaviour refuel();

    default ShipBehaviour cooldownActive(OffsetDateTime expiration) {
        Objects.requireNonNull(expiration, "expiration");

        return ShipBehaviour.ofResult(ShipBehaviourResult.waitUntil(expiration));
    }

    default ShipBehaviour inTransit(OffsetDateTime arrival) {
        Objects.requireNonNull(arrival, "arrival");

        return ShipBehaviour.ofResult(ShipBehaviourResult.waitUntil(arrival));
    }

    default ShipTreeBehaviour treeOf(ShipBehaviour... leaves) {
        return treeOf(List.of(leaves));
    }

    default ShipTreeBehaviour treeOf(List<ShipBehaviour> leaves) {
        inject(leaves);
        return new ShipTreeBehaviour(leaves);
    }

    @SuppressWarnings("unchecked")
    default void inject(Iterable<ShipBehaviour> leaves) {
        for (ShipBehaviour leaf : leaves) {
            if (leaf instanceof ShipBehaviourRefLeaf ref) {
                ref.setBehaviourFactory(this);
            } else if (leaf instanceof ShipBehaviourSequence sequence) {
                inject(sequence.behaviours());
            }
        }
    }

    FindMarketAndSellBehaviourFactory navigateToClosestMarket();

    SellCargoBehaviourFactory sellCargo(TradeSymbol tradeSymbol, int units);

}
