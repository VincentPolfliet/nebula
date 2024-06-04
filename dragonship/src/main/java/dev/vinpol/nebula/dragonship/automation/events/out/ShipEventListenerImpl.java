package dev.vinpol.nebula.dragonship.automation.events.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.vinpol.nebula.dragonship.utils.time.TimeWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
public class ShipEventListenerImpl implements ShipEventListener {

    private final Logger logger = LoggerFactory.getLogger(ShipEventListenerImpl.class);

    private final SimpMessagingTemplate template;
    private final TimeWizard clock;

    public ShipEventListenerImpl(SimpMessagingTemplate template, Clock clock) {
        this.template = template;
        this.clock = new TimeWizard(clock);
    }

    @Override
    @EventListener
    public void onCargoIsFull(CargoIsFullEvent event) {
        publish(event.shipSymbol(), event);
    }

    @Override
    @EventListener
    public void onFuelIsAlmostEmpty(FuelIsAlmostEmptyEvent event) {
        publish(event.shipSymbol(), event);
    }

    @Override
    @EventListener
    public void onWaitUntilArrival(NavigatingToEvent event) {
        publish(event.shipSymbol(), event);
    }

    @Override
    @EventListener
    public void onWaitCooldown(WaitUntilCooldownEvent event) {
        publish(event.shipSymbol(), event);
    }

    private void publish(String shipSymbol, Object payload) {
        var event = new JsonEvent(shipSymbol, clock.now(), payload);
        template.convertAndSend("/topic/ships", event);
        template.convertAndSend("/topic/ships/" + shipSymbol);
    }

    record JsonEvent(@JsonProperty("ship") String shipSymbol, @JsonProperty("timestamp") OffsetDateTime timestamp,
                     @JsonProperty("data") Object data) {

    }
}
