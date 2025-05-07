package dev.vinpol.nebula.dragonship.automation.events;

import dev.vinpol.nebula.dragonship.automation.events.out.CargoIsFullEvent;
import dev.vinpol.nebula.dragonship.automation.events.out.FuelIsAlmostEmptyEvent;
import dev.vinpol.nebula.dragonship.automation.events.out.NavigatingToEvent;
import dev.vinpol.nebula.dragonship.automation.events.out.WaitUntilCooldownEvent;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.utils.time.TimeWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
public class ShipEventNotifierImpl implements ShipEventNotifier {

    private final Logger logger = LoggerFactory.getLogger(ShipEventNotifierImpl.class);

    private final ApplicationEventPublisher eventPublisher;
    private final TimeWizard time;

    public ShipEventNotifierImpl(ApplicationEventPublisher eventPublisher, Clock clock) {
        this.eventPublisher = eventPublisher;
        this.time = new TimeWizard(clock);
    }

    @Override
    public void setNavigatingTo(String shipSymbol, WaypointSymbol waypointSymbol, OffsetDateTime arrival) {
        logEvent("WaitUntilArrival", shipSymbol);
        eventPublisher.publishEvent(new NavigatingToEvent(shipSymbol, waypointSymbol, arrival));
    }

    @Override
    public void setOrbited(String symbol) {
        logEvent("Orbited", symbol);
        eventPublisher.publishEvent(new OrbitedEvent(symbol));
    }

    @Override
    public void setDocked(String symbol) {
        logEvent("Docked", symbol);
        eventPublisher.publishEvent(new DockedEvent(symbol));
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
        if (!logger.isInfoEnabled()) {
            return;
        }

        logger.info("sending '{}' event for '{}' at '{}'", eventType, shipSymbol, time.now());
    }

}
