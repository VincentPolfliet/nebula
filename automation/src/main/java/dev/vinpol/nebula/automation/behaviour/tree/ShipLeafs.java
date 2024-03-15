package dev.vinpol.nebula.automation.behaviour.tree;

import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.torterra.DelegateLeaf;
import dev.vinpol.torterra.Leaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static dev.vinpol.torterra.Torterra.predicate;
import static java.util.function.Predicate.not;

public class ShipLeafs {
    private static final Logger logger = LoggerFactory.getLogger(ShipLeafs.class);

    private ShipLeafs() {

    }

    public static Leaf<Ship> cargoIsNotFull() {
        return new DelegateLeaf<>("Ship has place in cargo", predicate(not(ship -> {
            logger.debug("Checking if Ship has place in cargo");
            return ship.isCargoFull();
        })));
    }

    public static Leaf<Ship> hasNoActiveCooldown() {
        return new DelegateLeaf<>("Ship has no cooldown", predicate(not(Ship::hasActiveCooldown)));
    }

    public static Leaf<Ship> hasFuelLeft() {
        return new DelegateLeaf<>("Ship has fuel left", predicate(not(Ship::hasActiveCooldown)));
    }

    public static Leaf<Ship> isNotInTransit() {
        return new DelegateLeaf<>("Ship is not in transit", predicate(not(Ship::isInTransit)));
    }

    public static Leaf<Ship> isDocked() {
        return new DelegateLeaf<>("Ship is docked", predicate(Ship::isDocked));
    }
}
