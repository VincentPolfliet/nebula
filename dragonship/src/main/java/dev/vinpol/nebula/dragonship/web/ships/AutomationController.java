package dev.vinpol.nebula.dragonship.web.ships;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.vinpol.nebula.dragonship.shared.time.TimeWizard;
import dev.vinpol.nebula.dragonship.ships.CommandShipEvent;
import dev.vinpol.nebula.dragonship.ships.ShipStorage;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutomationController {

    private final ShipStorage shipStorage;
    private final TimeWizard timeWizard;
    private final ApplicationEventPublisher eventPublisher;

    public AutomationController(ShipStorage shipStorage, TimeWizard timeWizard, ApplicationEventPublisher eventPublisher) {
        this.shipStorage = shipStorage;
        this.timeWizard = timeWizard;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/api/automation")
    public void setInput(@RequestBody ShipCommandBody shipCommand) {
        Ship ship = shipStorage.retrieve(shipCommand.shipSymbol());
        eventPublisher.publishEvent(new CommandShipEvent(ship.getSymbol(), timeWizard.now()));
    }

    public record ShipCommandBody(
            @JsonProperty("ship") String shipSymbol
    ) {

    }
}
