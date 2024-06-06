package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResultAssert;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.sdk.*;
import dev.vinpol.nebula.dragonship.support.junit.TestHttpServer;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static dev.vinpol.nebula.dragonship.sdk.CooldownUtil.cooldown;
import static dev.vinpol.nebula.dragonship.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MiningBehaviourFactoryTest {

    private static TestHttpServer server;

    @Autowired
    private AutomationFactory registry;

    private WaypointGenerator waypointGenerator = new WaypointGenerator();

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("nebula.st.url", () -> server.url("/").toString());
        registry.add("nebula.st.token", () -> "token");
    }

    @BeforeAll
    static void setup() {
        server = new TestHttpServer();
    }

    @AfterAll
    static void close() throws Exception {
        server.close();
    }

    @Test
    void updateNoWaypointOfTypeInSystem() {
        enqueue(new GetSystemWaypoints200Response().data(Collections.emptyList()));

        SystemSymbol symbol = SystemSymbol.tryParse("DD-SYSTEM");
        WaypointType waypointType = WaypointType.ENGINEERED_ASTEROID;

        Ship ship = MotherShip.excavator();

        MiningBehaviourFactory factory = registry.miningAutomation(symbol, waypointType);
        ShipBehaviour behaviour = factory.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isDone()).isTrue();
    }

    @Test
    void shipNotAtLocationInOrbit() {
        Waypoint asteroidWaypoint = waypointGenerator.waypoint()
            .type(WaypointType.ENGINEERED_ASTEROID);

        WaypointSymbol asteroidWaypointSymbol = WaypointSymbol.tryParse(asteroidWaypoint.getSymbol());
        SystemSymbol system = asteroidWaypointSymbol.toSystemSymbol();

        MiningBehaviourFactory factory = registry.miningAutomation(system, asteroidWaypoint.getType());
        ShipBehaviour behaviour = factory.create();

        Ship ship = MotherShip.excavator();
        Waypoint currentLocation = waypointGenerator.waypoint()
            .systemSymbol(asteroidWaypointSymbol.system())
            .symbol(asteroidWaypointSymbol.system() + "-CURRENT_LOCATION");
        WaypointSymbol currentLocationSymbol = WaypointSymbol.tryParse(currentLocation.getSymbol());

        ship.withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
            nav.setSystemSymbol(currentLocationSymbol.system());
            nav.setWaypointSymbol(currentLocationSymbol.waypoint());
        });

        enqueue(new GetSystemWaypoints200Response().data(List.of(asteroidWaypoint)));

        // orbit
        Ship afterOrbitShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip.getNav())));

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);

        // navigate
        Ship afterNavigationShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setWaypointSymbol(asteroidWaypointSymbol.waypoint());
            nav.withRoute(route -> {
                route.setDepartureTime(OffsetDateTime.now());
                route.arrival(OffsetDateTime.now());
                route.destination(new ShipNavRouteWaypoint().symbol(asteroidWaypointSymbol.waypoint()).systemSymbol(asteroidWaypointSymbol.system()).type(asteroidWaypoint.getType()));
            });
        });

        afterNavigationShip.withFuel(fuel -> {
            fuel.setCurrent(69);
        });

        enqueue(new GetWaypoint200Response().data(currentLocation));
        enqueue(new GetWaypoint200Response().data(asteroidWaypoint));
        enqueue(new GetShipNav200Response().data(afterNavigationShip.getNav()));

        enqueue(new NavigateShip200Response()
            .data(
                new NavigateShip200ResponseData()
                    .nav(afterNavigationShip.getNav())
                    .fuel(afterNavigationShip.getFuel())
            ));

        ShipBehaviourResult navigateResult = behaviour.update(ship);
        assertThat(navigateResult.isWaitUntil()).isTrue();
        assertThat(((WaitUntil) navigateResult).timestamp()).isEqualTo(afterNavigationShip.getNav().getRoute().getArrival());

        assertThat(ship.getNav().getWaypointSymbol()).isEqualTo(afterNavigationShip.getNav().getWaypointSymbol());

        // dock
        Ship afterDockingShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.waypointSymbol(asteroidWaypointSymbol.waypoint());
            nav.systemSymbol(asteroidWaypointSymbol.system());
            nav.setStatus(ShipNavStatus.DOCKED);
        });

        enqueue(new DockShip200Response().data(new ShipNavModifiedResponseData().nav(afterDockingShip.getNav())));

        ShipBehaviourResult dockResult = behaviour.update(ship);
        assertThat(dockResult.isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.DOCKED);

        // refuel
        Ship afterRefueling = ShipCloner.clone(ship).withFuel(fuel -> {
            fuel.setCurrent(100);
            fuel.setCapacity(100);
        });

        asteroidWaypoint.addTraitsItem(WaypointTraits.market());
        enqueue(new GetWaypoint200Response().data(asteroidWaypoint));
        enqueue(new GetMarket200Response().data(new Market().addExportsItem(TradeGoods.fuel())));

        enqueue(new RefuelShip200Response()
            .data(
                new RefuelShip200ResponseData()
                    .fuel(afterRefueling.getFuel())
            )
        );

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getFuel().isFull()).isTrue();

        // orbit
        Ship afterOrbitShip2 = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip2.getNav())));

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);

        // extraction
        Ship afterExtractionShip = ShipCloner.clone(ship)
            .cooldown(cooldown(OffsetDateTime.now()))
            .withCargo(cargo -> {
                cargo.addInventoryItem(cargoItem(TradeSymbol.IRON));
            });

        enqueue(
            new ExtractResources201Response()
                .data(
                    new ExtractResources201ResponseData()
                        .cargo(afterExtractionShip.getCargo())
                        .cooldown(afterExtractionShip.getCooldown())
                ),
            201
        );

        ShipBehaviourResult extractionResult = behaviour.update(ship);
        assertThat(extractionResult.isWaitUntil()).isTrue();
    }

    @Test
    void shipAtLocationInOrbit() {
        SystemSymbol symbol = SystemSymbol.tryParse("DD-SYSTEM");
        WaypointType waypointType = WaypointType.ENGINEERED_ASTEROID;

        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(symbol.system() + "-" + waypointType);

        Ship ship = MotherShip.excavator();

        ship.withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
            nav.setWaypointSymbol(waypointSymbol.waypoint());
        });

        MiningBehaviourFactory factory = registry.miningAutomation(symbol, waypointType);
        ShipBehaviour behaviour = factory.create();

        enqueue(new GetSystemWaypoints200Response().data(List.of(new Waypoint().symbol(symbol.system() + "-" + waypointType).type(waypointType))));

        // orbit
        Ship afterOrbitShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip.getNav())));

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);

        // try navigate
        behaviour.update(ship);

        // extraction
        Ship afterExtractionShip = ShipCloner.clone(ship)
            .cooldown(cooldown(OffsetDateTime.now()))
            .withCargo(cargo -> {
                cargo.addInventoryItem(cargoItem(TradeSymbol.IRON));
            });

        enqueue(
            new ExtractResources201Response()
                .data(
                    new ExtractResources201ResponseData()
                        .cargo(afterExtractionShip.getCargo())
                        .cooldown(afterExtractionShip.getCooldown())
                ),
            201
        );

        ShipBehaviourResult extractionResult = behaviour.update(ship);
        ShipBehaviourResultAssert.assertThat(extractionResult).isWaitUntil();
    }

    @Test
    void shipAtLocationDocked() {
        SystemSymbol symbol = SystemSymbol.tryParse("DD-SYSTEM");
        WaypointType waypointType = WaypointType.ENGINEERED_ASTEROID;

        WaypointSymbol waypointSymbol = WaypointSymbol.tryParse(symbol.system() + "-" + waypointType);

        Ship ship = MotherShip.excavator();

        ship.withNav(nav -> {
            nav.setStatus(ShipNavStatus.DOCKED);
            nav.setWaypointSymbol(waypointSymbol.waypoint());
        });

        MiningBehaviourFactory factory = registry.miningAutomation(symbol, waypointType);
        ShipBehaviour behaviour = factory.create();

        enqueue(new GetSystemWaypoints200Response().data(List.of(new Waypoint().symbol(symbol.system() + "-" + waypointType).type(waypointType))));

        // orbit
        Ship afterOrbitShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip.getNav())));

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);

        // tryNavigate
        ShipBehaviourResultAssert.assertThat(behaviour.update(ship)).isSuccess();

        // extraction
        Ship afterExtractionShip = ShipCloner.clone(ship)
            .cooldown(cooldown(OffsetDateTime.now()))
            .withCargo(cargo -> {
                cargo.addInventoryItem(cargoItem(TradeSymbol.IRON));
            });

        enqueue(
            new ExtractResources201Response()
                .data(
                    new ExtractResources201ResponseData()
                        .cargo(afterExtractionShip.getCargo())
                        .cooldown(afterExtractionShip.getCooldown())
                ),
            201
        );

        ShipBehaviourResult extractionResult = behaviour.update(ship);
        assertThat(extractionResult.isWaitUntil()).isTrue();
    }


    private static void enqueue(Object object) {
        enqueue(object, 200);
    }


    private static void enqueue(Object object, int responseCode) {
        server.enqueue(object, responseCode);
    }

}
