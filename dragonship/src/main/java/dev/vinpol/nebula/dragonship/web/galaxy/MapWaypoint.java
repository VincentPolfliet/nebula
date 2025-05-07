package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MapWaypoint(
    /*
      Unique identifier of the WayPoint
     */
    @JsonProperty("symbol") String symbol,
    @JsonProperty("type") String type,
    @JsonProperty("x") double x,
    @JsonProperty("y") double y,
    @JsonProperty("priority") int priority,
    @JsonProperty("orbits") String orbits
) {

}
