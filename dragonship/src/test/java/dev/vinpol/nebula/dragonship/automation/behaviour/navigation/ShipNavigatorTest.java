package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.spacetraders.sdk.models.Waypoint;
import dev.vinpol.spacetraders.sdk.models.WaypointTrait;
import dev.vinpol.spacetraders.sdk.models.WaypointTraitSymbol;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class ShipNavigatorTest {

    private static final Set<Waypoint> WAYPOINTS = new HashSet<>();
    private static final Logger log = LoggerFactory.getLogger(ShipNavigatorTest.class);

    WaypointGenerator generator = new WaypointGenerator();
    RouteGraphCalculator calculator = new RouteGraphCalculator(new TravelCostCalculator(), new RouteGraphCalculator.Config(15));

    @BeforeAll
    static void setup() throws Exception {
        record DataWaypoint(String symbol, int x, int y, String type, List<String> traits) {

        }

        try (var input = ShipNavigatorTest.class.getResourceAsStream("/waypoints.json")) {
            ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
            List<DataWaypoint> dataWaypoints = mapper.readValue(input, new TypeReference<>() {
            });

            List<Waypoint> waypoints = dataWaypoints
                .stream()
                .map(d -> {
                    Waypoint w = new Waypoint()
                        .symbol(d.symbol())
                        .x(d.x())
                        .y(d.y())
                        .type(WaypointType.valueOf(d.type()));

                    for (String trait : d.traits()) {
                        w.addTraitsItem(
                            new WaypointTrait()
                                .name(trait)
                                .description(trait)
                                .symbol(WaypointTraitSymbol.valueOf(trait))
                        );
                    }

                    return w;
                })
                .toList();

            ShipNavigatorTest.WAYPOINTS.addAll(waypoints);
        }
    }

    @Test
    void test2() {
        Waypoint origin = getWaypoint("X1-RQ94-CX5E");

        Graph<String, TravelEdge> graph = calculator.getGraph(ShipNavigatorTest.WAYPOINTS);
        debugGraph(graph);

        for (Waypoint waypoint : WAYPOINTS) {
            if (!Objects.equals(waypoint, origin)) {
                Assertions.assertTrue(graph.containsVertex(waypoint.getSymbol()));

                TravelEdge edge = graph.getEdge(origin.getSymbol(), waypoint.getSymbol());

                if (edge == null) {
                    log.info("{} -> {} is null", origin.getSymbol(), waypoint.getSymbol());
                }

                Assertions.assertNotNull(edge);
            }
        }
    }

    private static void debugGraph(Graph<String, TravelEdge> graph) {
        for (String w : graph.vertexSet()) {
            log.info("Vertex: {}", w);
        }

        for (TravelEdge e : graph.edgeSet()) {
            log.info("Edge: {} -> {}", graph.getEdgeSource(e), graph.getEdgeTarget(e));
        }
    }

    private Waypoint getWaypoint(String s) {
        return ShipNavigatorTest.WAYPOINTS.stream()
            .filter(w -> w.getSymbol().equals(s))
            .findFirst()
            .orElseThrow();
    }
}
