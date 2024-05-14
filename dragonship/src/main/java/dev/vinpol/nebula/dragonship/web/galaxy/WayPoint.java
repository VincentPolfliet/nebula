package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WayPoint(
    /*
      Unique identifier of the WayPoint
     */
    @JsonProperty("symbol") String symbol,
    /*
      Type of the waypoint, waypoints of the same type will be grouped together. It's case-sensitive.
     */
    @JsonProperty("type") String type,
    @JsonProperty("x") double x,
    @JsonProperty("y") double y,
    @JsonProperty("priority") int priority
) {
    public WayPoint(String symbol, String type, double x, double y) {
        this(symbol, type, x, y, 0);
    }
}
