package dev.vinpol.spacetraders.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * A cooldown is a period of time in which a ship cannot perform certain actions.
 */
@Data
public class Cooldown {
    private String shipSymbol;
    /**
     * The total duration of the cooldown in seconds
     * minimum: 0
     */
    private Integer totalSeconds;
    /**
     * The remaining duration of the cooldown in seconds
     * minimum: 0
     **/
    private Integer remainingSeconds;
    private OffsetDateTime expiration;

    public static Cooldown noCooldown(String shipSymbol) {
        return new Cooldown()
            .shipSymbol(shipSymbol)
            .totalSeconds(0)
            .remainingSeconds(0)
            .expiration(null);
    }

    public Cooldown shipSymbol(String shipSymbol) {
        this.shipSymbol = shipSymbol;
        return this;
    }

    public Cooldown totalSeconds(Integer totalSeconds) {
        this.totalSeconds = totalSeconds;
        return this;
    }

    public Cooldown remainingSeconds(Integer remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
        return this;
    }

    public Cooldown expiration(OffsetDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    @JsonIgnore
    public boolean isActive() {
        return expiration != null;
    }
}

