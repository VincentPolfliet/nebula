package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;

import java.util.concurrent.CompletableFuture;

interface ShipBehaviourTask extends Runnable {
    String shipSymbol();

    ShipBehaviour behaviour();

    CompletableFuture<ShipBehaviorResult> future();
}
