package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WayPointType(
    @JsonProperty("title") String title,
    @JsonProperty("type") String type
) {
}
