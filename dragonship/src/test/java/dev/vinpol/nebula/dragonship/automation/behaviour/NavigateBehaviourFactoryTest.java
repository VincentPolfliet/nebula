package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.events.ShipEventNotifier;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.WaitUntil;
import dev.vinpol.nebula.dragonship.sdk.WaypointSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class NavigateBehaviourFactoryTest {

    FleetApi fleetApi = mock(FleetApi.class);
    ShipEventNotifier shipEventNotifier = mock(ShipEventNotifier.class);
    WaypointSymbol waypointSymbol = WaypointSymbol.tryParse("XX-XD-XDBD");

    NavigateBehaviourFactory sut;

    @BeforeEach
    void setup() {
        sut = new NavigateBehaviourFactory(fleetApi, shipEventNotifier, waypointSymbol);
    }

    @Test
    void test() {
        OffsetDateTime arrival = OffsetDateTime.now();

        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav
                        .status(ShipNavStatus.IN_ORBIT)
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

        when(fleetApi.navigateShip(eq(ship.getSymbol()), any(NavigateShipRequest.class)))
            .thenReturn(new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .fuel(expected.getFuel())
                    .nav(expected.getNav())
                ));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.waitUntil()).isEqualTo(arrival);
        });

        verify(shipEventNotifier).setWaitUntilArrival(ship.getSymbol(), arrival);
    }

    @Test
    void testWithLowFuel() {
        OffsetDateTime arrival = OffsetDateTime.now();

        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav
                        .status(ShipNavStatus.IN_ORBIT)
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

        when(fleetApi.navigateShip(eq(ship.getSymbol()), any(NavigateShipRequest.class)))
            .thenReturn(new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .fuel(expected.getFuel())
                    .nav(expected.getNav())
                ));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.waitUntil()).isEqualTo(arrival);
        });

        verify(shipEventNotifier).setFuelIsAlmostEmpty(ship.getSymbol());
        verify(shipEventNotifier).setWaitUntilArrival(ship.getSymbol(), arrival);
    }

    @Test
    void testWithEmptyFuel() {
        OffsetDateTime arrival = OffsetDateTime.now();

        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav
                        .status(ShipNavStatus.IN_ORBIT)
            );

        Ship expected = ShipCloner.clone(ship);
        expected.withNav(nav -> {
                nav.withRoute(route -> {
                    route.arrival(arrival);
                });
            })
            .fuel(
                new ShipFuel()
                    .current(0)
                    .capacity(100)
            );

        when(fleetApi.navigateShip(eq(ship.getSymbol()), any(NavigateShipRequest.class)))
            .thenReturn(new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .fuel(expected.getFuel())
                    .nav(expected.getNav())
                ));

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isWaitUntil()).isTrue();
        assertThat(result).isInstanceOfSatisfying(WaitUntil.class, waitUntil -> {
            assertThat(waitUntil.waitUntil()).isEqualTo(arrival);
        });

        verify(shipEventNotifier).setFuelIsAlmostEmpty(ship.getSymbol());
        verify(shipEventNotifier).setWaitUntilArrival(ship.getSymbol(), arrival);
    }


    @Test
    void shipNotInOrbit() {
        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav
                        .status(ShipNavStatus.DOCKED)
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.NOT_IN_ORBIT)).isTrue();

        verifyNoInteractions(shipEventNotifier);
    }

    @Test
    void shipAlreadyAtLocation() {
        Ship ship = MotherShip.excavator()
            .withNav(
                nav ->
                    nav
                        .withRoute(route -> {
                            route.destination(
                                new ShipNavRouteWaypoint()
                                    .symbol(waypointSymbol.waypoint())
                            );
                        })
                        .status(ShipNavStatus.IN_ORBIT)
            );

        ShipBehaviour behaviour = sut.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.ALREADY_AT_LOCATION)).isTrue();

        verifyNoInteractions(shipEventNotifier);
    }
}
