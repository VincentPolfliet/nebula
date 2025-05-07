package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.nebula.dragonship.automation.behaviour.AutomationFactory;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviorResult;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.ships.TravelCostCalculator;
import dev.vinpol.nebula.dragonship.support.junit.TestHttpServer;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;

class FindMarketAndSellBehaviourFactoryTest {

    FindMarketAndSellBehaviourFactory sut;

    private final WaypointGenerator waypointGenerator = new WaypointGenerator();

    @BeforeEach
    void beforeEach() {
        sut = new FindMarketAndSellBehaviourFactory(
            mock(AutomationFactory.class),
            mock(SystemsApi.class),
            mock(TravelCostCalculator.class)
        );
    }

    @Test
    void noCargoInShip() {
        ShipBehaviour behaviour = sut.create();

        Ship excavator = MotherShip.excavator();
        excavator.getCargo().setInventory(Collections.emptyList());

        ShipBehaviorResult result = behaviour.update(excavator);

        assertThat(result.hasFailedWithReason(FailureReason.NO_CARGO_TO_SELL)).isTrue();
    }

    @Test
    void noMarketsInSystem() {
        ShipBehaviour behaviour = sut.create();

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

        ShipBehaviorResult result = behaviour.update(excavator);
        assertThat(result.hasFailedWithReason(FailureReason.NO_WAYPOINTS_FOUND_IN_CURRENT_SYSTEM)).isTrue();
    }
}
