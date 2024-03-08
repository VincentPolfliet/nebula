package dev.vinpol.nebula.dragonship.ships;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShips200Response;
import dev.vinpol.spacetraders.sdk.models.Meta;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ShipSynchronizeTaskTest {

    private ShipStorage shipStorage;
    private FleetApi fleetApi;

    ShipSynchronizeTask sut;

    @BeforeEach
    void setup() {
        shipStorage = mock(ShipStorage.class);
        fleetApi = mock(FleetApi.class);
        sut = new ShipSynchronizeTask(shipStorage, fleetApi);
    }

    @Test
    void run() {
        Ship excavator = ShipMother.excavator();

        GetMyShips200Response shipsResponse = new GetMyShips200Response();
        shipsResponse.data(
            List.of(excavator)
        );

        shipsResponse.meta(
            new Meta()
                .limit(1)
                .page(10)
                .total(1)
        );

        when(fleetApi.getMyShips(1, 10)).thenReturn(shipsResponse);

        sut.run();
        verify(shipStorage).store(excavator.getSymbol(), excavator);
    }

    @Test
    void runNoShips() {
        GetMyShips200Response shipsResponse = new GetMyShips200Response();
        shipsResponse.data(
            Collections.emptyList()
        );

        shipsResponse.meta(
            new Meta()
                .limit(1)
                .page(10)
                .total(1)
        );

        when(fleetApi.getMyShips(1, 10)).thenReturn(shipsResponse);

        sut.run();
        verifyNoInteractions(shipStorage);
    }


    @Test
    void runMultipleShips() {
        GetMyShips200Response shipsResponse = new GetMyShips200Response();
        shipsResponse.data(
            List.of(
                ShipMother.excavator(),
                ShipMother.excavator(),
                ShipMother.excavator()
            )
        );

        shipsResponse.meta(
            new Meta()
                .limit(1)
                .page(10)
                .total(3)
        );

        when(fleetApi.getMyShips(1, 10)).thenReturn(shipsResponse);

        sut.run();

        ArgumentCaptor<Ship> shipArgumentCaptor = ArgumentCaptor.forClass(Ship.class);
        verify(shipStorage, times(3)).store(anyString(), shipArgumentCaptor.capture());

        List<Ship> allShips = shipArgumentCaptor.getAllValues();
        assertThat(allShips).containsAll(shipsResponse.getData());
    }
}
