package dev.vinpol.nebula.dragonship.sdk;

import dev.vinpol.spacetraders.sdk.models.Waypoint;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.instancio.Gen;
import org.instancio.Instancio;
import org.instancio.generator.specs.StringSpec;

import static org.instancio.Select.field;

public class WaypointGenerator {

    private final StringSpec symbolStringSpec = Gen.string().alphaNumeric().allowEmpty(false);

    public WaypointSymbol waypointSymbol() {
        String sector = symbolStringSpec.length(2).get();
        String system = symbolStringSpec.length(4).get();
        String waypoint = symbolStringSpec.length(6).get();

        return WaypointSymbol.tryParse(sector + "-" + system + "-" + waypoint);
    }

    public Waypoint waypoint() {
        WaypointSymbol waypointSymbol = waypointSymbol();

        return generateWaypoint(waypointSymbol);
    }

    public Waypoint waypointOf(String waypointSymbolStr) {
        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(waypointSymbolStr);
        return generateWaypoint(waypointSymbol);
    }

    private static Waypoint generateWaypoint(WaypointSymbol waypointSymbol) {
        return Instancio.of(Waypoint.class)
            .supply(field(Waypoint::getSymbol), waypointSymbol::waypoint)
            .supply(field(Waypoint::getSystemSymbol), waypointSymbol::system)
            .generate(field(Waypoint::getType), gen -> gen.enumOf(WaypointType.class))
            .generate(field(Waypoint::getX), gen -> gen.ints().range(-100, 100))
            .generate(field(Waypoint::getY), gen -> gen.ints().range(-100, 100))
            .create();
    }
}
