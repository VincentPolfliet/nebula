package dev.vinpol.nebula.automation.behaviour;

import dev.vinpol.nebula.automation.ShipCloner;
import dev.vinpol.nebula.automation.ShipEventNotifier;
import dev.vinpol.nebula.automation.sdk.ApiClientStub;
import dev.vinpol.nebula.automation.sdk.SystemSymbol;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static dev.vinpol.nebula.automation.sdk.ShipCargoUtil.cargo;
import static dev.vinpol.nebula.automation.sdk.ShipCargoUtil.cargoItem;
import static org.assertj.core.api.Assertions.assertThat;
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
    void slop() {
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
}
