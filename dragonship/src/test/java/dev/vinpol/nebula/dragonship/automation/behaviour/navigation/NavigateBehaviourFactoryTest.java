package dev.vinpol.nebula.dragonship.automation.behaviour.navigation;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NavigateBehaviourFactoryTest {

    private final WaypointGenerator waypointGenerator = new WaypointGenerator();

    FleetApi fleetApi = mock(FleetApi.class);
    SystemsApi systemsApi = mock(SystemsApi.class);

    TravelFuelAndTimerCalculator travelFuelAndTimerCalculator = spy(new TravelFuelAndTimerCalculator());
    ShipEventNotifier shipEventNotifier = mock(ShipEventNotifier.class);

    NavigationShipBehaviour sut;
    WaypointSymbol targetWaypointSymbol;
    Waypoint waypoint;

    @BeforeEach
    void setup() {
        targetWaypointSymbol = waypointGenerator.waypointSymbol();


        waypoint = waypointGenerator.waypoint().type(WaypointType.MOON);
        sut = new NavigationShipBehaviour(fleetApi, systemsApi, travelFuelAndTimerCalculator, shipEventNotifier, targetWaypointSymbol);
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


        when(travelFuelAndTimerCalculator.selectBestFlightMode(anyMap(), anyMap())).thenReturn(ShipNavFlightMode.CRUISE);

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(arrival);
        });

        assertThat(ship.getNav().getFlightMode()).isEqualTo(ShipNavFlightMode.CRUISE);

        verify(shipEventNotifier).setWaitUntilArrival(ship.getSymbol(), arrival);
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

        when(travelFuelAndTimerCalculator.selectBestFlightMode(ship.getEngine(), waypoint, waypoint)).thenReturn(ShipNavFlightMode.CRUISE);

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.timestamp()).isEqualTo(arrival);
        });

        assertThat(ship.getNav().getFlightMode()).isEqualTo(ShipNavFlightMode.CRUISE);

        verify(shipEventNotifier).setFuelIsAlmostEmpty(ship.getSymbol());
        verify(shipEventNotifier).setWaitUntilArrival(ship.getSymbol(), arrival);
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
