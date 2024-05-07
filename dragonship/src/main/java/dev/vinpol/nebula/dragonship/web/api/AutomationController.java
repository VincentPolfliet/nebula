package dev.vinpol.nebula.dragonship.web.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.vinpol.nebula.dragonship.ships.CommandShipEvent;
import dev.vinpol.nebula.dragonship.ships.ShipCache;
import dev.vinpol.nebula.dragonship.time.TimeWizard;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
public class AutomationController {

    private final ShipCache shipCache;
    private final TimeWizard timeWizard;
    private final ApplicationEventPublisher eventPublisher;

    public AutomationController(ShipCache shipCache, Clock clock, ApplicationEventPublisher eventPublisher) {
        this.shipCache = shipCache;
        this.timeWizard = new TimeWizard(clock);
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/api/automation")
    public void setInput(@RequestBody ShipCommandBody shipCommand) {
        Ship ship = shipCache.retrieve(shipCommand.shipSymbol());
        eventPublisher.publishEvent(new CommandShipEvent(ship.getSymbol(), timeWizard.now()));
    }

    public record ShipCommandBody(
            @JsonProperty("ship") String shipSymbol
    ) {

    }
}
