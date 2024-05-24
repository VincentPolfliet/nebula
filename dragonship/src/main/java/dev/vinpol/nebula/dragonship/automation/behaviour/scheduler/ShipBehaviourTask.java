package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;

import java.util.concurrent.CompletableFuture;

public interface ShipBehaviourTask extends Runnable {
    String shipSymbol();

    ShipBehaviour behaviour();

    CompletableFuture<ShipBehaviourResult> future();
}
