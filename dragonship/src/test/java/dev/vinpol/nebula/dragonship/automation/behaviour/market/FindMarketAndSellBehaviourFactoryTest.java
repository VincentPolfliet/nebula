package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.support.junit.TestHttpServer;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Collections;

import static dev.vinpol.nebula.dragonship.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FindMarketAndSellBehaviourFactoryTest {

    private static TestHttpServer SERVER = new TestHttpServer();

    @Autowired
    AutomationFactory shipBehaviourFactoryCreator;

    private final WaypointGenerator waypointGenerator = new WaypointGenerator();

    @BeforeAll
    static void beforeAll() {
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("nebula.st.url", () -> SERVER.url("/").toString());
        registry.add("nebula.st.token", () -> "token");
    }

    @Test
    void noCargoInShip() {
        FindMarketAndSellBehaviourFactory factory = shipBehaviourFactoryCreator.navigateToClosestMarket();
        ShipBehaviour behaviour = factory.create();

        Ship excavator = MotherShip.excavator();
        excavator.getCargo().setInventory(Collections.emptyList());

        ShipBehaviourResult result = behaviour.update(excavator);

        assertThat(result.hasFailedWithReason(FailureReason.NO_CARGO_TO_SELL)).isTrue();
    }

    @Test
    void noMarketsInSystem() {
        FindMarketAndSellBehaviourFactory factory = shipBehaviourFactoryCreator.navigateToClosestMarket();
        ShipBehaviour behaviour = factory.create();

        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse("XX-XXX1-LEUK");

        Ship excavator = MotherShip.excavator();

        excavator.withCargo(cargo -> {
            cargo.setUnits(2);
            cargo.setCapacity(100);
            cargo.addInventoryItem(cargoItem(TradeSymbol.IRON).units(2));
        });

        excavator.withNav(nav -> {
            nav.systemSymbol(waypointSymbol.system())
                .waypointSymbol(waypointSymbol.waypoint());
        });

        enqueue(new GetSystemWaypoints200Response().data(Collections.emptyList()).meta(new Meta().page(1).limit(10).total(0)));

        ShipBehaviourResult result = behaviour.update(excavator);

        assertThat(result.hasFailedWithReason(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM)).isTrue();
    }

    private void enqueue(Object data) {
        SERVER.enqueue(data);
    }
}
