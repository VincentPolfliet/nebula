package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WayPoint(
    @JsonProperty("symbol") String symbol,
    @JsonProperty("type") String type,
    @JsonProperty("x") int x,
    @JsonProperty("y") int y
) {
}
