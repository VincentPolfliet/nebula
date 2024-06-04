package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
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

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static dev.vinpol.nebula.dragonship.automation.sdk.CooldownUtil.cooldown;
import static dev.vinpol.nebula.dragonship.automation.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DirtiesContext
@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MiningBehaviourFactoryTest {

    private static TestHttpServer server;

    @Autowired
    private ShipBehaviourFactoryCreator registry;

    private WaypointGenerator waypointGenerator = new WaypointGenerator();

    @BeforeAll
    static void setup() {
        server = new TestHttpServer();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("nebula.st.url", () -> server.url("/").toString());
        registry.add("nebula.st.token", () -> "token");
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
            nav.setWaypointSymbol(currentLocationSymbol.waypoint());
        });

        enqueue(new GetSystemWaypoints200Response().data(List.of(asteroidWaypoint)));

        // check in orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check at location
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

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

        // not docked check
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // dock
        Ship afterDockingShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setWaypointSymbol(asteroidWaypointSymbol.waypoint());
            nav.setStatus(ShipNavStatus.DOCKED);
        });

        enqueue(new DockShip200Response().data(new ShipNavModifiedResponseData().nav(afterDockingShip.getNav())));

        ShipBehaviourResult dockResult = behaviour.update(ship);
        assertThat(dockResult.isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.DOCKED);

        // check if we need to refuel
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // refuel
        Ship afterRefueling = ShipCloner.clone(ship).withFuel(fuel -> {
            fuel.setCurrent(100);
            fuel.setCapacity(100);
        });

        enqueue(new RefuelShip200Response()
            .data(
                new RefuelShip200ResponseData()
                    .fuel(afterRefueling.getFuel())
            )
        );

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getFuel().isFull()).isTrue();

        // not in orbit check
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // orbit
        Ship afterOrbitShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip.getNav())));

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

        // check in orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check at location
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check is not docked
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // dock
        enqueue(new DockShip200Response()
            .data(new ShipNavModifiedResponseData()
                .nav(ShipCloner.clone(ship).getNav().status(ShipNavStatus.DOCKED)))
        );

        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check fuel is not full
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check not in orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // orbit
        Ship afterOrbitShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip.getNav())));

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

        // check in orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // orbit
        Ship afterOrbitShip = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitShip.getNav())));

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.IN_ORBIT);

        // check at location
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check is not docked
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // dock
        enqueue(new DockShip200Response()
            .data(new ShipNavModifiedResponseData()
                .nav(ShipCloner.clone(ship).getNav().status(ShipNavStatus.DOCKED)))
        );

        assertThat(behaviour.update(ship).isSuccess()).isTrue();
        assertThat(ship.getNav().getStatus()).isEqualTo(ShipNavStatus.DOCKED);

        // fuel is not full
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // not in orbit check
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // orbit again
        Ship afterOrbitAgain = ShipCloner.clone(ship).withNav(nav -> {
            nav.setStatus(ShipNavStatus.IN_ORBIT);
        });

        enqueue(new OrbitShip200Response().data(new ShipNavModifiedResponseData().nav(afterOrbitAgain.getNav())));

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


    private static void enqueue(Object object) {
        enqueue(object, 200);
    }


    private static void enqueue(Object object, int responseCode) {
        server.enqueue(object, responseCode);
    }

}
