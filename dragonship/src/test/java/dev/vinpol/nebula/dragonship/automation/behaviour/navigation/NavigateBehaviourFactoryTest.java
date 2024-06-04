package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NavigateBehaviourFactoryTest {

    private final WaypointGenerator waypointGenerator = new WaypointGenerator();

    FleetApi fleetApi = mock(FleetApi.class);
    SystemsApi systemsApi = mock(SystemsApi.class);

    FlightModeOptimizer optimizer = mock(FlightModeOptimizer.class);
    ShipEventNotifier shipEventNotifier = mock(ShipEventNotifier.class);

    NavigationShipBehaviour sut;
    WaypointSymbol targetWaypointSymbol;
    Waypoint waypoint;

    @BeforeEach
    void setup() {
        targetWaypointSymbol = waypointGenerator.waypointSymbol();

        waypoint = waypointGenerator.waypoint().type(WaypointType.MOON);
        sut = new NavigationShipBehaviour(fleetApi, systemsApi, optimizer, shipEventNotifier, targetWaypointSymbol);
    }

    @Test
    void test() {
        WaypointSymbol currentLocation = waypointGenerator.waypointSymbol();
        Waypoint currentWaypoint = waypointGenerator.waypoint()
            .symbol(currentLocation.waypoint())
            .systemSymbol(currentLocation.system());

        OffsetDateTime arrival = OffsetDateTime.now();

        Ship ship = MotherShip.excavator()
            .withNav(
                nav -> {
                    nav.status(ShipNavStatus.IN_ORBIT);
                    nav.systemSymbol(currentLocation.system());
                    nav.waypointSymbol(currentLocation.waypoint());
                }
            );

        Ship expected = ShipCloner.clone(ship);
        expected.withNav(nav -> {
                nav.withRoute(route -> {
                    route.arrival(arrival);
                });
            })
            .fuel(
                new ShipFuel()
                    .current(75)
                    .capacity(100)
            );

        when(systemsApi.getWaypoint(currentLocation.system(), currentLocation.waypoint())).thenReturn(
            new GetWaypoint200Response()
                .data(currentWaypoint)
        );

        when(systemsApi.getWaypoint(targetWaypointSymbol.system(), targetWaypointSymbol.waypoint())).thenReturn(
            new GetWaypoint200Response()
                .data(waypoint)
        );

        when(fleetApi.navigateShip(eq(ship.getSymbol()), any(NavigateShipRequest.class)))
            .thenReturn(new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .fuel(expected.getFuel())
                    .nav(expected.getNav())
                ));


        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(arrival);
        });

        verify(shipEventNotifier).setNavigatingTo(ship.getSymbol(), targetWaypointSymbol, arrival);
    }

    @Test
    void testWithLowFuel() {
        WaypointSymbol currentLocation = waypointGenerator.waypointSymbol();
        Waypoint currentWaypoint = waypointGenerator.waypoint()
            .symbol(currentLocation.waypoint())
            .systemSymbol(currentLocation.system());

        OffsetDateTime arrival = OffsetDateTime.now();

        Ship ship = MotherShip.excavator()
            .withNav(
                nav -> {
                    nav.status(ShipNavStatus.IN_ORBIT);
                    nav.systemSymbol(currentLocation.system());
                    nav.waypointSymbol(currentLocation.waypoint());
                }
            );

        Ship expected = ShipCloner.clone(ship);
        expected.withNav(nav -> {
                nav.withRoute(route -> {
                    route.arrival(arrival);
                });
            })
            .fuel(
                new ShipFuel()
                    .current(20)
                    .capacity(100)
            );

        when(systemsApi.getWaypoint(currentLocation.system(), currentLocation.waypoint())).thenReturn(
            new GetWaypoint200Response()
                .data(currentWaypoint)
        );

        when(systemsApi.getWaypoint(targetWaypointSymbol.system(), targetWaypointSymbol.waypoint())).thenReturn(
            new GetWaypoint200Response()
                .data(waypoint)
        );

        when(fleetApi.navigateShip(eq(ship.getSymbol()), any(NavigateShipRequest.class)))
            .thenReturn(new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .fuel(expected.getFuel())
                    .nav(expected.getNav())
                ));

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(arrival);
        });

        assertThat(ship.getNav().getFlightMode()).isEqualTo(ShipNavFlightMode.CRUISE);

        verify(shipEventNotifier).setFuelIsAlmostEmpty(ship.getSymbol());
        verify(shipEventNotifier).setNavigatingTo(ship.getSymbol(), targetWaypointSymbol, arrival);
    }

    @Test
    void testWithEmptyFuel() {
        Ship ship = MotherShip.excavator()
            .fuel(
                new ShipFuel()
                    .current(0)
                    .capacity(100)
            );

        ShipBehaviourResult result = sut.update(ship);
        assertThat(result.hasFailedWithReason(FailureReason.FUEL_IS_EMPTY)).isTrue();
    }

    @Test
    void testWithInfiniteFuel() {
        Ship ship = MotherShip.satellite();

        WaypointSymbol currentLocation = waypointGenerator.waypointSymbol();
        Waypoint currentWaypoint = waypointGenerator.waypoint()
            .symbol(currentLocation.waypoint())
            .systemSymbol(currentLocation.system());

        ship.withNav(nav -> {
            nav.waypointSymbol(currentLocation.waypoint())
                .systemSymbol(currentLocation.system());
        });

        OffsetDateTime arrival = OffsetDateTime.now();

        when(systemsApi.getWaypoint(currentLocation.system(), currentLocation.waypoint())).thenReturn(
            new GetWaypoint200Response()
                .data(currentWaypoint)
        );

        when(systemsApi.getWaypoint(targetWaypointSymbol.system(), targetWaypointSymbol.waypoint())).thenReturn(
            new GetWaypoint200Response()
                .data(waypoint)
        );

        when(fleetApi.navigateShip(eq(ship.getSymbol()), any(NavigateShipRequest.class)))
            .thenReturn(new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .fuel(ship.getFuel())
                    .nav(ShipCloner.clone(ship).withNav(nav -> nav.withRoute(route -> route.arrival(arrival))).getNav())
                ));

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(arrival);
        });

        verify(shipEventNotifier).setFuelIsAlmostEmpty(ship.getSymbol());
        verify(shipEventNotifier).setNavigatingTo(ship.getSymbol(), targetWaypointSymbol, arrival);
    }


    @Test
    void shipNotInOrbit() {
        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav
                        .status(ShipNavStatus.DOCKED)
            );

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.NOT_IN_ORBIT)).isTrue();

        verifyNoInteractions(shipEventNotifier);
    }

    @Test
    void shipAlreadyAtLocation() {
        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav.waypointSymbol(targetWaypointSymbol.waypoint())
                        .status(ShipNavStatus.IN_ORBIT)
            );

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.ALREADY_AT_LOCATION)).isTrue();

        verifyNoInteractions(shipEventNotifier);
    }
}
