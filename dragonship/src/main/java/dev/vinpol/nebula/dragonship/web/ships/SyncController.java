package dev.vinpol.nebula.dragonship.web.ships;

import dev.vinpol.nebula.dragonship.ships.SyncEvent;
import dev.vinpol.nebula.dragonship.web.StatusMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {

    private final ApplicationEventPublisher eventPublisher;

    public SyncController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/api/sync")
    public ResponseEntity<StatusMessage> sync() {
        eventPublisher.publishEvent(new SyncEvent());
        return ResponseEntity.ok(new StatusMessage("Action scheduled successfully"));
    }
}
