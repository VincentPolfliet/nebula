package dev.vinpol.nebula.dragonship.web.api;

import dev.failsafe.RateLimiter;
import dev.vinpol.nebula.dragonship.ships.SyncEvent;
import dev.vinpol.nebula.dragonship.web.StatusMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class SyncController {

    private final ApplicationEventPublisher eventPublisher;

    private final RateLimiter<Object> rateLimiter = RateLimiter.burstyBuilder(1, Duration.ofSeconds(5))
        .build();

    public SyncController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/api/sync")
    public ResponseEntity<StatusMessage> sync() {
        eventPublisher.publishEvent(new SyncEvent());
        return ResponseEntity.ok(new StatusMessage("Action scheduled successfully"));
    }
}
