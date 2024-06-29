package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;


import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import org.jgrapht.Graph;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RouteGraphCalculatorTest {

    WaypointGenerator generator = new WaypointGenerator();

    @Test
    void run() {
        Waypoint origin = getWaypointOrThrow("X1-RQ94-H48");
        Waypoint target = getWaypointOrThrow("X1-RQ94-CX5E");

        RouteGraphCalculator calculator = new RouteGraphCalculator(new TravelCostCalculator(), new RouteGraphCalculator.Config(15));
        Graph<String, TravelEdge> graph = calculator.getGraph(Set.of(origin, target));

        assertThat(graph.containsVertex(origin.getSymbol())).isTrue();
        assertThat(graph.containsVertex(target.getSymbol())).isTrue();
        assertThat(graph.containsEdge(origin.getSymbol(), target.getSymbol())).isTrue();
    }

    private Waypoint getWaypointOrThrow(String waypointSymbol) {
        return generator.waypointOf(waypointSymbol);
    }

}
