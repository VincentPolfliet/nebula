package dev.vinpol.nebula.automation;

import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipRole;

import java.util.*;

public class WillItBlendReasonBuilder {
    private ShipRole role;
    private final Set<CanItRunReason> mapped = new HashSet<>();

    public Collection<CanItRunReason> getNotMapped() {
        var all = new HashSet<>(EnumSet.allOf(CanItRunReason.class));
        all.removeAll(mapped);
        return Collections.unmodifiableCollection(all);
    }

    public Collection<CanItRunReason> getReasons() {
        return Collections.unmodifiableCollection(mapped);
    }

    public WillItBlendReasonBuilder withRole(ShipRole role, Ship ship) {
        this.role = role;

        if (ship.getRegistration().getRole() != role) {
            this.mapped.add(CanItRunReason.ILLEGAL_ROLE);
        }

        return this;
    }

    public WillItBlendReasonBuilder withReason(CanItRunReason reason) {
        Objects.requireNonNull(reason);
        mapped.add(reason);
        return this;
    }

    public boolean hasReason(CanItRunReason reason) {
        Objects.requireNonNull(reason);
        return mapped.contains(reason);
    }
}
