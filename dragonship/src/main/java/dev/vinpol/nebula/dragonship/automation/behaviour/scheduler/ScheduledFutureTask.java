package dev.vinpol.nebula.dragonship.automation.behaviour.scheduler;

import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

final class ScheduledFutureTask implements ShipBehaviourTask {

    private final String shipSymbol;
    private final OffsetDateTime scheduledAt;
    private final CompletableFuture<ShipBehaviourResult> future;

    public ScheduledFutureTask(String shipSymbol, OffsetDateTime scheduledAt, CompletableFuture<ShipBehaviourResult> future) {
        this.shipSymbol = shipSymbol;
        this.scheduledAt = scheduledAt;
        this.future = future;
    }

    @Override
    public String shipSymbol() {
        return shipSymbol;
    }

    @Override
    public ShipBehaviour behaviour() {
        return ShipBehaviour.ofResult(new WaitUntil(scheduledAt));
    }

    @Override
    public CompletableFuture<ShipBehaviourResult> future() {
        return future;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException();
    }
}
