package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.nebula.dragonship.automation.behaviour.navigation.Route;

import java.util.List;

public record RouteSimulation(String origin, String target, List<String> waypoints, Route route) {
}
