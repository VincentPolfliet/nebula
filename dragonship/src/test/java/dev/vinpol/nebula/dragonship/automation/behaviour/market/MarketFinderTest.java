package dev.vinpol.nebula.dragonship.automation.behaviour.market;

import dev.vinpol.nebula.dragonship.sdk.ShipCargoUtil;
import dev.vinpol.nebula.dragonship.sdk.TradeGoods;
import dev.vinpol.nebula.dragonship.sdk.WaypointGenerator;
import dev.vinpol.nebula.dragonship.sdk.WaypointTraits;
import dev.vinpol.nebula.dragonship.ships.TravelFuelAndTimerCalculator;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MarketFinderTest {

    private final WaypointGenerator waypointGenerator = new WaypointGenerator();

    private Ship ship;
    private Waypoint currentLocation;

    SystemsApi systemsApi;
    TravelFuelAndTimerCalculator calculator;

    MarketFinder sut;

    @BeforeEach
    void setup() {
        systemsApi = mock(SystemsApi.class);
        calculator = mock(TravelFuelAndTimerCalculator.class);

        sut = new MarketFinder(systemsApi, calculator);

        ship = MotherShip.excavator();
        currentLocation = waypointGenerator.waypoint();
    }

    @Test
    void getBestMarketForCurrentCargo() {
        assertThat(sut.getBestMarketForCurrentCargo(ship, currentLocation, Collections.emptyList())).isNull();
    }

    @Test
    void getBestMarketForCurrentCargo2() {
        Waypoint marketWaypoint = waypointGenerator.waypoint();
        marketWaypoint.addTraitsItem(WaypointTraits.market());

        ship.withCargo(cargo -> {
            cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL).units(5));
        });

        Market market = new Market();
        market.addImportsItem(TradeGoods.fuel());

        when(systemsApi.getMarket(marketWaypoint.getSystemSymbol(), marketWaypoint.getSymbol())).thenReturn(new GetMarket200Response().data(market));

        MarketWaypoint bestMarket = sut.getBestMarketForCurrentCargo(ship, currentLocation, List.of(marketWaypoint));

        assertThat(bestMarket.waypoint()).isEqualTo(marketWaypoint);
        assertThat(bestMarket.canSellScore()).isEqualTo(5);
    }

    @Test
    void getBestMarketForCurrentCargo3() {
        Waypoint marketWaypoint1 = waypointGenerator.waypoint();
        marketWaypoint1.addTraitsItem(WaypointTraits.market());

        Waypoint marketWaypoint2 = waypointGenerator.waypoint();
        marketWaypoint2.addTraitsItem(WaypointTraits.market());

        ship.withCargo(cargo -> {
            cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL).units(5));
            cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.ASSAULT_RIFLES).units(1));
        });

        Market market1 = new Market();
        market1.addImportsItem(TradeGoods.fuel());

        Market market2 = new Market();
        market2.addImportsItem(TradeGoods.fuel());
        market2.addImportsItem(TradeGoods.ofSymbol(TradeSymbol.ASSAULT_RIFLES));

        when(systemsApi.getMarket(marketWaypoint1.getSystemSymbol(), marketWaypoint1.getSymbol())).thenReturn(new GetMarket200Response().data(market1));
        when(systemsApi.getMarket(marketWaypoint2.getSystemSymbol(), marketWaypoint2.getSymbol())).thenReturn(new GetMarket200Response().data(market2));

        when(calculator.calculateDistance(any(), any())).thenReturn(0d);

        MarketWaypoint bestMarket = sut.getBestMarketForCurrentCargo(ship, currentLocation, List.of(marketWaypoint1, marketWaypoint2));

        assertThat(bestMarket.waypoint().getSymbol()).isEqualTo(marketWaypoint2.getSymbol());
        assertThat(bestMarket.canSellScore()).isEqualTo(6);
    }

    @Test
    void getBestMarketForCurrentCargo4() {
        Waypoint marketWaypoint1 = waypointGenerator.waypoint();
        marketWaypoint1.addTraitsItem(WaypointTraits.market());

        Waypoint marketWaypoint2 = waypointGenerator.waypoint();
        marketWaypoint2.addTraitsItem(WaypointTraits.market());

        ship.withCargo(cargo -> {
            cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL).units(5));
        });

        Market market1 = new Market();
        market1.addImportsItem(TradeGoods.fuel());

        Market market2 = new Market();
        market2.addImportsItem(TradeGoods.fuel());

        when(systemsApi.getMarket(marketWaypoint1.getSystemSymbol(), marketWaypoint1.getSymbol())).thenReturn(new GetMarket200Response().data(market1));
        when(systemsApi.getMarket(marketWaypoint2.getSystemSymbol(), marketWaypoint2.getSymbol())).thenReturn(new GetMarket200Response().data(market2));

        when(calculator.calculateDistance(any(), any()))
            .thenReturn(1d)
            .thenReturn(0d);

        MarketWaypoint bestMarket = sut.getBestMarketForCurrentCargo(ship, currentLocation, List.of(marketWaypoint1, marketWaypoint2));

        assertThat(bestMarket.waypoint().getSymbol()).isEqualTo(marketWaypoint2.getSymbol());
        assertThat(bestMarket.canSellScore()).isEqualTo(5);
    }

    @Test
    void getBestMarketForCurrentCargo5() {
        Waypoint marketWaypoint = waypointGenerator.waypoint();
        marketWaypoint.addTraitsItem(WaypointTraits.market());

        ship.withCargo(cargo -> {
            cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL).units(5));
        });

        Market market = new Market();

        when(systemsApi.getMarket(marketWaypoint.getSystemSymbol(), marketWaypoint.getSymbol())).thenReturn(new GetMarket200Response().data(market));

        MarketWaypoint bestMarket = sut.getBestMarketForCurrentCargo(ship, currentLocation, Collections.singletonList(marketWaypoint));
        assertThat(bestMarket).isNull();
    }

    @Test
    void calculateSellScoreWithCargoHasMarketImports() {
        Market market = new Market();
        market.addImportsItem(TradeGoods.fuel());

        ShipCargo cargo = new ShipCargo();
        cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL).units(5));

        int score = MarketFinder.calculateSellScore(market, cargo);

        assertThat(score).isEqualTo(5);
    }

    @Test
    void calculateSellScoreWithCargoHasNoImports() {
        Market market = new Market();
        market.addImportsItem(TradeGoods.fuel());

        ShipCargo cargo = new ShipCargo();

        int score = MarketFinder.calculateSellScore(market, cargo);

        assertThat(score).isEqualTo(0);
    }

    @Test
    void calculateSellScoreWithMarketHasNoImports() {
        Market market = new Market();

        ShipCargo cargo = new ShipCargo();
        cargo.addInventoryItem(ShipCargoUtil.cargoItem(TradeSymbol.FUEL).units(5));

        int score = MarketFinder.calculateSellScore(market, cargo);

        assertThat(score).isEqualTo(0);
    }
}
