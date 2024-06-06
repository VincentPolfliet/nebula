package dev.vinpol.nebula.dragonship.automation.behaviour;

import dev.vinpol.nebula.dragonship.automation.ShipCloner;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.FailureReason;
import dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.dragonship.sdk.ShipCargoUtil;
import dev.vinpol.nebula.dragonship.sdk.TradeGoods;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.sdk.WaypointTraits;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static dev.vinpol.nebula.dragonship.automation.behaviour.state.ShipBehaviourResultAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RefuelShipBehaviourTest {

    private final WaypointGenerator generator = new WaypointGenerator();

    FleetApi fleetApi = mock(FleetApi.class);
    SystemsApi systemsApi = mock(SystemsApi.class);

    RefuelShipBehaviour sut;

    @BeforeEach
    void setup() {
        sut = new RefuelShipBehaviour(fleetApi, systemsApi);
    }

    @Test
    void shipIsDockedAndHasFuelSpace() {
        Waypoint currentLocationWaypoint = generator.waypoint();
        currentLocationWaypoint.addTraitsItem(WaypointTraits.market());

        Ship ship = MotherShip.excavator()
            .withNav(nav -> {
                nav.docked()
                    .waypointSymbol(currentLocationWaypoint.getSymbol())
                    .systemSymbol(currentLocationWaypoint.getSystemSymbol());
            })
            .fuel(new ShipFuel().current(69).capacity(100));

        Ship expected = ShipCloner.clone(ship)
            .fuel(new ShipFuel()
                .current(100)
                .capacity(100)
            );

        when(systemsApi.getWaypoint(ship.getNav().getSystemSymbol(), ship.getNav().getWaypointSymbol()))
            .thenReturn(new GetWaypoint200Response().data(currentLocationWaypoint));

        when(systemsApi.getMarket(ship.getNav().getSystemSymbol(), ship.getNav().getWaypointSymbol()))
            .thenReturn(new GetMarket200Response().data(new Market().addExportsItem(TradeGoods.fuel())));

        when(fleetApi.refuelShip(eq(ship.getSymbol()), any(RefuelShipRequest.class)))
            .thenReturn(
                new RefuelShip200Response()
                    .data(
                        new RefuelShip200ResponseData()
                            .fuel(expected.getFuel())
                    )
            );

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isDone();
        assertThat(ship).isEqualTo(expected);

        ArgumentCaptor<RefuelShipRequest> requestCaptor = ArgumentCaptor.forClass(RefuelShipRequest.class);
        verify(fleetApi).refuelShip(eq(ship.getSymbol()), requestCaptor.capture());

        RefuelShipRequest request = requestCaptor.getValue();
        assertThat(request.getUnits()).isNull();
        assertThat(request.isFromCargo()).isFalse();
    }

    @Test
    void shipIsDockedAndHasFuelSpaceWithFuelInCargo() {
        Waypoint currentLocationWaypoint = generator.waypoint();
        currentLocationWaypoint.addTraitsItem(WaypointTraits.market());

        Ship ship = MotherShip.excavator()
            .withNav(nav -> {
                nav.docked()
                    .waypointSymbol(currentLocationWaypoint.getSymbol())
                    .systemSymbol(currentLocationWaypoint.getSystemSymbol());
            })
            .fuel(new ShipFuel().current(69).capacity(100))
            .withCargo(cargo -> {
                cargo.units(1);
                cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL));
            });

        Ship expected = ShipCloner.clone(ship)
            .fuel(new ShipFuel()
                .current(100)
                .capacity(100)
            );

        when(systemsApi.getWaypoint(ship.getNav().getSystemSymbol(), ship.getNav().getWaypointSymbol()))
            .thenReturn(new GetWaypoint200Response().data(currentLocationWaypoint));

        when(systemsApi.getMarket(ship.getNav().getSystemSymbol(), ship.getNav().getWaypointSymbol()))
            .thenReturn(new GetMarket200Response().data(new Market().addExportsItem(TradeGoods.fuel())));

        when(fleetApi.refuelShip(eq(ship.getSymbol()), any(RefuelShipRequest.class)))
            .thenReturn(
                new RefuelShip200Response()
                    .data(
                        new RefuelShip200ResponseData()
                            .fuel(expected.getFuel())
                    )
            );

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result).isDone();
        assertThat(ship).isEqualTo(expected);

        ArgumentCaptor<RefuelShipRequest> requestCaptor = ArgumentCaptor.forClass(RefuelShipRequest.class);
        verify(fleetApi).refuelShip(eq(ship.getSymbol()), requestCaptor.capture());

        RefuelShipRequest request = requestCaptor.getValue();
        assertThat(request.getUnits()).isNull();
        assertThat(request.isFromCargo()).isTrue();
    }


    @Test
    void shipIsDockedAndHasNoFuelSpace() {
        Ship ship = MotherShip.excavator()
            .nav(new ShipNav().status(ShipNavStatus.DOCKED))
            .fuel(new ShipFuel().current(100).capacity(100));

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.FUEL_IS_FULL)).isTrue();
    }

    @Test
    void shipIsInOrbit() {
        Ship ship = MotherShip.excavator()
            .nav(new ShipNav().status(ShipNavStatus.IN_ORBIT))
            .fuel(new ShipFuel().current(100).capacity(100));

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.NOT_DOCKED)).isTrue();
    }

    @Test
    void shipIsInTransit() {
        Ship ship = MotherShip.excavator()
            .nav(new ShipNav().status(ShipNavStatus.IN_TRANSIT))
            .fuel(new ShipFuel().current(100).capacity(100));

        ShipBehaviourResult result = sut.update(ship);

        assertThat(result.hasFailedWithReason(FailureReason.NOT_DOCKED)).isTrue();
    }
}
