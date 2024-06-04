package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.support.junit.TestHttpServer;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static dev.vinpol.nebula.dragonship.automation.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FindMarketAndSellBehaviourFactoryTest {

    private static TestHttpServer SERVER = new TestHttpServer();

    @Autowired
    ShipBehaviourFactoryCreator shipBehaviourFactoryCreator;

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

    @Test
    void marketsInSystemAndInOrbit() {
        FindMarketAndSellBehaviourFactory factory = shipBehaviourFactoryCreator.navigateToClosestMarket();
        ShipBehaviour behaviour = factory.create();

        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse("XX-XXX1-LEUK");
        Waypoint waypoint = waypointGenerator.waypoint();
        waypoint.symbol(waypointSymbol.waypoint())
            .systemSymbol(waypointSymbol.system());

        Ship excavator = MotherShip.excavator();

        excavator.withFuel(fuel -> {
            fuel.setCapacity(100);
            fuel.setCurrent(100);
        });

        excavator.withCargo(cargo -> {
            cargo.setUnits(2);
            cargo.setCapacity(100);
            cargo.addInventoryItem(cargoItem(TradeSymbol.IRON).units(2));
        });

        excavator.withNav(nav -> {
            nav.status(ShipNavStatus.IN_ORBIT);
            nav.systemSymbol(waypointSymbol.system())
                .waypointSymbol(waypointSymbol.waypoint());
        });

        enqueue(new GetSystemWaypoints200Response().data(
            List.of(
                new Waypoint()
                    .symbol(waypointSymbol.system() + "-market")
                    .systemSymbol(waypointSymbol.system())
                    .addTraitsItem(
                        new WaypointTrait()
                            .name("market")
                            .symbol(WaypointTraitSymbol.MARKETPLACE)
                    )
            )
        ).meta(new Meta().total(1).page(1).limit(10)));

        // in orbit check
        assertThat(behaviour.update(excavator).isSuccess()).isTrue();

        // not at location
        ShipBehaviourResult notAtLocation = behaviour.update(excavator);
        assertThat(notAtLocation.isSuccess()).isTrue();

        // navigate
        enqueue(
            new GetWaypoint200Response()
                .data(waypoint)
        );

        enqueue(
            new GetWaypoint200Response()
                .data(new Waypoint()
                    .x(15)
                    .y(15)
                    .symbol(waypointSymbol.system() + "-market")
                    .systemSymbol(waypointSymbol.system())
                    .addTraitsItem(
                        new WaypointTrait()
                            .name("market")
                            .symbol(WaypointTraitSymbol.MARKETPLACE)
                    ))
        );

        enqueue(
            new GetShipNav200Response()
                .data(excavator.getNav())
        );

        OffsetDateTime arrivalDateTime = OffsetDateTime.now();

        enqueue(new NavigateShip200Response()
            .data(new NavigateShip200ResponseData()
                .fuel(excavator.getFuel())
                .nav(
                    new ShipNav()
                        .status(ShipNavStatus.IN_ORBIT)
                        .withRoute(route -> {
                            route.arrival(arrivalDateTime);
                        })
                )
            )
        );

        ShipBehaviourResult navigate = behaviour.update(excavator);

        assertThat(navigate.isWaitUntil()).isTrue();
        assertThat(navigate).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(arrivalDateTime);
        });

        assertThat(excavator.getNav().getRoute().getArrival()).isEqualTo(arrivalDateTime);
        assertThat(excavator.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);

        // not docked check
        assertThat(behaviour.update(excavator).isSuccess()).isTrue();

        enqueue(new DockShip200Response()
            .data(
                new ShipNavModifiedResponseData()
                    .nav(ShipCloner.clone(excavator).getNav().status(ShipNavStatus.DOCKED))
            )
        );

        // dock
        assertThat(behaviour.update(excavator).isSuccess()).isTrue();
        assertThat(excavator.getNav().getStatus()).isEqualTo(ShipNavStatus.DOCKED);

        // sell cargo
        enqueue(
            new SellCargo201Response()
                .data(
                    new SellCargo201ResponseData()
                        .cargo(
                            new ShipCargo()
                        )
                )
        );

        assertThat(behaviour.update(excavator).isSuccess()).isTrue();
        assertThat(excavator.getCargo().isEmpty()).isTrue();

        // fuel check
        assertThat(behaviour.update(excavator).isSuccess()).isTrue();

        // Done
        assertThat(behaviour.update(excavator).isDone()).isTrue();
    }

    private void enqueue(Object data) {
        SERVER.enqueue(data);
    }
}
