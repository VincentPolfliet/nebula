package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.automation.events.CargoIsFullEvent;
import dev.vinpol.nebula.dragonship.automation.events.FuelIsAlmostEmptyEvent;
import dev.vinpol.nebula.dragonship.automation.events.WaitUntilArrivalEvent;
import dev.vinpol.nebula.dragonship.automation.events.WaitUntilCooldownEvent;
import dev.vinpol.nebula.dragonship.time.TimeWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.time.OffsetDateTime;

public class ShipEventNotifierImpl implements ShipEventNotifier {

    private final Logger logger = LoggerFactory.getLogger(ShipEventNotifierImpl.class);

    private final ApplicationEventPublisher eventPublisher;
    private final TimeWizard time;

    public ShipEventNotifierImpl(ApplicationEventPublisher eventPublisher, Clock clock) {
        this.eventPublisher = eventPublisher;
        this.time = new TimeWizard(clock);
    }

    @Override
    public void setWaitUntilArrival(String shipSymbol, OffsetDateTime arrival) {
        logEvent("WaitUntilArrival", shipSymbol);
        eventPublisher.publishEvent(new WaitUntilArrivalEvent(shipSymbol, arrival));
    }

    @Override
    public void setWaitUntilCooldown(String shipSymbol, OffsetDateTime expiration) {
        logEvent("WaitUntilCooldown", shipSymbol);
        eventPublisher.publishEvent(new WaitUntilCooldownEvent(shipSymbol, expiration));
    }

    @Override
    public void setCargoFull(String shipSymbol) {
        logEvent("CargoFull", shipSymbol);
        eventPublisher.publishEvent(new CargoIsFullEvent(shipSymbol));
    }

    @Override
    public void setFuelIsAlmostEmpty(String shipSymbol) {
        logEvent("FuelIsAlmostEmpty", shipSymbol);
        eventPublisher.publishEvent(new FuelIsAlmostEmptyEvent(shipSymbol));
    }


    private void logEvent(String eventType, String shipSymbol) {
        if (!logger.isTraceEnabled()) {
            return;
        }

        logger.trace("sending '{}' event for '{}' at '{}'", eventType, shipSymbol, time.now());
    }

}
