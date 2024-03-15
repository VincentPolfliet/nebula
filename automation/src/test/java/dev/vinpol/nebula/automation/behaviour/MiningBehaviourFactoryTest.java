package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.ShipCloner;
import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.behaviour.state.ShipBehaviourResult;
import dev.vinpol.nebula.automation.sdk.ApiClientStub;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.System;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static dev.vinpol.nebula.automation.sdk.ShipCargoUtil.cargo;
import static dev.vinpol.nebula.automation.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MiningBehaviourFactoryTest {

    private BehaviourFactoryRegistry registry;
    private ApiClientStub apiClient;

    @BeforeEach
    void setup() {
        apiClient = new ApiClientStub();
        registry = new DefaultBehaviourFactoryRegistry(apiClient, mock(ShipEventNotifier.class));
    }

    @Test
    void updateNoWaypointOfTypeInSystem() {
        SystemSymbol symbol = SystemSymbol.tryParse("DD-SYSTEM");
        WaypointType waypointType = WaypointType.ENGINEERED_ASTEROID;

        Ship ship = ShipMother.excavator();

        SystemsApi systemsApi = apiClient.systemsApi();
        when(systemsApi.getSystemWaypoints(symbol.system(), 1, 10, waypointType, new String[]{}))
            .thenReturn(
                new GetSystemWaypoints200Response()
            );

        MiningBehaviourFactory factory = new MiningBehaviourFactory(systemsApi, registry, symbol, waypointType);
        ShipBehaviour behaviour = factory.create();
        ShipBehaviourResult result = behaviour.update(ship);

        assertThat(result.isDone()).isTrue();
    }

    @Test
    void waypointInSystemAndShipIsAtLocation() {
        SystemSymbol systemSymbol = SystemSymbol.tryParse("DD-SYSTEM");
        WaypointType waypointType = WaypointType.ENGINEERED_ASTEROID;
        Ship ship = ShipMother.excavator();
        String waypointSymbol = systemSymbol.system() + "-" + waypointType.name().toLowerCase();

        ship.getNav()
            .withRoute(
                route ->
                    route.withDestination(
                        destination ->
                            destination
                                .systemSymbol(systemSymbol.system())
                                .symbol(waypointSymbol)
                                .type(waypointType)
                    )
            );

        Ship original = ShipCloner.clone(ship);

        SystemsApi systemsApi = apiClient.systemsApi();
        when(systemsApi.getSystemWaypoints(systemSymbol.system(), 1, 10, waypointType, new String[]{}))
            .thenReturn(
                new GetSystemWaypoints200Response()
                    .addDataItem(new Waypoint()
                        .symbol(waypointSymbol)
                        .systemSymbol(systemSymbol.system())
                    )
            );

        FleetApi fleetApi = apiClient.fleetApi();

        ShipCargo currentCargo = original.getCargo();

        // lamo this line
        // TODO fix
        ExtractResources201ResponseData extractionResource = new ExtractResources201ResponseData()
            .cargo(
                cargo(currentCargo.getCapacity(), currentCargo.getCapacity())
                    .addInventoryItem(cargoItem(TradeSymbol.COPPER_ORE).units(currentCargo.getCapacity()
                    ))
            )
            .cooldown(
                new Cooldown()
                    .remainingSeconds(59)
                    .totalSeconds(60)
                    .expiration(OffsetDateTime.now().plusSeconds(59))
            );

        when(fleetApi.extractResources(ship.getSymbol(), new ExtractResourcesRequest()))
            .thenReturn(new ExtractResources201Response().data(extractionResource));

        Ship shipAfterExtraction = ShipCloner.clone(original);
        shipAfterExtraction
            .cooldown(extractionResource.getCooldown())
            .cargo(extractionResource.getCargo());

        MiningBehaviourFactory factory = new MiningBehaviourFactory(systemsApi, registry, systemSymbol, waypointType);
        ShipBehaviour behaviour = factory.create();

        // check for cargo
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check for cooldown
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check for fuel
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // check for not in transit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship is NOT docked, so don't go into orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship is already at location
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship does an extraction
        assertThat(behaviour.update(ship).isWaitUntil()).isTrue();
    }

    @Test
    void waypointInSystemAndShipIsNotAtLocation() {
        SystemSymbol systemSymbol = SystemSymbol.tryParse("DD-SYSTEM");
        WaypointType waypointType = WaypointType.ENGINEERED_ASTEROID;
        String waypointSymbol = systemSymbol.system() + "-" + waypointType.name().toLowerCase();

        Ship ship = ShipMother.excavator()
            .withNav(nav -> {
                nav.withRoute(
                    route ->
                        route.withDestination(
                            destination ->
                                destination
                                    .systemSymbol(systemSymbol.system())
                                    .symbol(systemSymbol.system() + WaypointType.NEBULA.name().toLowerCase()) // other waypoint
                                    .type(WaypointType.NEBULA)
                        )
                );
            });

        int currentCargoCapacity = ship.getCargo().getCapacity();

        Ship expected = ShipCloner.clone(ship)
            .withNav(nav -> {
                nav.withRoute(route -> {
                    route.arrival(OffsetDateTime.now().plusSeconds(5));
                    route.withDestination(destination -> {
                        destination
                            .symbol(systemSymbol.system())
                            .symbol(waypointSymbol)
                            .type(waypointType);
                    });
                });
            })
            .withFuel(fuel -> {
                fuel.current(75)
                    .capacity(100);
            })
            .withCooldown(cooldown -> {
                cooldown
                    .remainingSeconds(59)
                    .totalSeconds(60)
                    .expiration(OffsetDateTime.now().plusSeconds(59));
            })
            .withCargo(cargo -> {
                cargo.capacity(ship.getCargo().getCapacity())
                    .addInventoryItem(cargoItem(TradeSymbol.COPPER_ORE).units(currentCargoCapacity));
            });

        SystemsApi systemsApi = apiClient.systemsApi();
        when(systemsApi.getSystemWaypoints(systemSymbol.system(), 1, 10, waypointType, new String[]{})).thenReturn(
            new GetSystemWaypoints200Response()
                .addDataItem(new Waypoint()
                    .symbol(waypointSymbol)
                    .systemSymbol(systemSymbol.system())
                )
        );

        FleetApi fleetApi = apiClient.fleetApi();

        when(fleetApi.navigateShip(ship.getSymbol(), new NavigateShipRequest().waypointSymbol(waypointSymbol))).thenReturn(
            new NavigateShip200Response()
                .data(new NavigateShip200ResponseData()
                    .nav(expected.getNav())
                    .fuel(expected.getFuel())
                )
        );

        when(fleetApi.dockShip(ship.getSymbol())).thenReturn(
            new DockShip200Response()
                .data(
                    new OrbitShip200ResponseData()
                        .nav(ShipCloner.clone(expected).getNav().status(ShipNavStatus.DOCKED))
                )
        );

        when(fleetApi.refuelShip(eq(ship.getSymbol()), any(RefuelShipRequest.class)))
            .thenReturn(
                new RefuelShip200Response()
                    .data(new RefuelShip200ResponseData()
                        .fuel(
                            new ShipFuel()
                                .capacity(100)
                                .current(100)
                        )
                    )
            );

        when(fleetApi.orbitShip(ship.getSymbol()))
            .thenReturn(new OrbitShip200Response()
                .data(new OrbitShip200ResponseData()
                    .nav(new ShipNav().status(ShipNavStatus.IN_ORBIT))
                )
            );

        when(fleetApi.extractResources(ship.getSymbol(), new ExtractResourcesRequest())).thenReturn(
            new ExtractResources201Response().data(new ExtractResources201ResponseData()
                .cargo(expected.getCargo())
                .cooldown(expected.getCooldown())
            )
        );

        MiningBehaviourFactory factory = new MiningBehaviourFactory(systemsApi, registry, systemSymbol, waypointType);
        ShipBehaviour behaviour = factory.create();

        // Ship has place in cargo
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship has no cooldown
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship has fuel left
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship is not in transit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // Ship is docked
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // navigate
        assertThat(behaviour.update(ship).isWaitUntil()).isTrue();

        // dock
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // refuel
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // orbit
        assertThat(behaviour.update(ship).isSuccess()).isTrue();

        // extraction
        assertThat(behaviour.update(ship).isWaitUntil()).isTrue();

        // finished
        assertThat(behaviour.update(ship).isDone()).isTrue();
    }
}
