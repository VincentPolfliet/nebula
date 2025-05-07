package dev.vinpol.nebula.dragonship.bigbrain;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.*;
import org.dizitart.no2.Nitrite;
import retrofit2.Call;

public class FleetApiCache implements FleetApi {

    private final FleetApi fleetApi;
    private final NitriteApiCache<String, Ship> cache;

    public FleetApiCache(FleetApi fleetApi, Nitrite db) {
        this.fleetApi = fleetApi;
        this.cache = new NitriteApiCache<>(db, Ship.class, "symbol");
    }

    @Override
    public Call<CreateChart201Response> createChart(String shipSymbol) {
        return fleetApi.createChart(shipSymbol);
    }

    @Override
    public Call<CreateShipShipScan201Response> createShipShipScan(String shipSymbol) {
        return fleetApi.createShipShipScan(shipSymbol);
    }

    @Override
    public Call<CreateShipSystemScan201Response> createShipSystemScan(String shipSymbol) {
        return fleetApi.createShipSystemScan(shipSymbol);
    }

    @Override
    public Call<CreateShipWaypointScan201Response> createShipWaypointScan(String shipSymbol) {
        return fleetApi.createShipWaypointScan(shipSymbol);
    }

    @Override
    public Call<CreateSurvey201Response> createSurvey(String shipSymbol) {
        return fleetApi.createSurvey(shipSymbol);
    }

    @Override
    public DockShip200Response dockShip(String shipSymbol) {
        DockShip200Response dockShipResponse = fleetApi.dockShip(shipSymbol);

        cache.updateIfExists(shipSymbol, ship -> {
            ship.setNav(dockShipResponse.getData().getNav());
        });
        return dockShipResponse;
    }

    @Override
    public ExtractResources201Response extractResources(String shipSymbol, ExtractResourcesRequest extractResourcesRequest) {
        return fleetApi.extractResources(shipSymbol, extractResourcesRequest);
    }

    @Override
    public Call<ExtractResources201Response> extractResourcesWithSurvey(String shipSymbol, Survey survey) {
        return fleetApi.extractResourcesWithSurvey(shipSymbol, survey);
    }

    @Override
    public Call<GetMounts200Response> getMounts(String shipSymbol) {
        return fleetApi.getMounts(shipSymbol);
    }

    @Override
    public GetMyShip200Response getMyShip(String shipSymbol) {
        return cache.getByIdAsOptional(shipSymbol)
            .map(shipRecord -> new GetMyShip200Response().data(shipRecord))
            .orElseGet(() -> {
                GetMyShip200Response shipResponse = fleetApi.getMyShip(shipSymbol);

                Ship ship = shipResponse.getData();
                cache.updateOrInsert(shipSymbol, ship);
                return shipResponse;
            });
    }

    @Override
    public Call<GetMyShipCargo200Response> getMyShipCargo(String shipSymbol) {
        return fleetApi.getMyShipCargo(shipSymbol);
    }

    @Override
    public GetMyShips200Response getMyShips(Integer page, Integer limit) {
        // don't cache pagination
        return fleetApi.getMyShips(page, limit);
    }

    @Override
    public Call<GetShipCooldown200Response> getShipCooldown(String shipSymbol) {
        return fleetApi.getShipCooldown(shipSymbol);
    }

    @Override
    public Call<GetShipNav200Response> getShipNav(String shipSymbol) {
        return fleetApi.getShipNav(shipSymbol);
    }

    @Override
    public Call<InstallMount201Response> installMount(String shipSymbol, InstallMountRequest installMountRequest) {
        return fleetApi.installMount(shipSymbol, installMountRequest);
    }

    @Override
    public Call<Jettison200Response> jettison(String shipSymbol, JettisonRequest jettisonRequest) {
        return fleetApi.jettison(shipSymbol, jettisonRequest);
    }

    @Override
    public Call<JumpShip200Response> jumpShip(String shipSymbol, JumpShipRequest jumpShipRequest) {
        return fleetApi.jumpShip(shipSymbol, jumpShipRequest);
    }

    @Override
    public NavigateShip200Response navigateShip(String shipSymbol, NavigateShipRequest navigateShipRequest) {
        NavigateShip200Response navigateResponse = fleetApi.navigateShip(shipSymbol, navigateShipRequest);

        cache.updateIfExists(shipSymbol, ship -> {
            ship.setFuel(navigateResponse.getData().getFuel());
            ship.setNav(navigateResponse.getData().getNav());
        });

        return navigateResponse;
    }

    @Override
    public Call<NegotiateContract200Response> negotiateContract(String shipSymbol) {
        return fleetApi.negotiateContract(shipSymbol);
    }

    @Override
    public OrbitShip200Response orbitShip(String shipSymbol) {
        OrbitShip200Response orbitResponse = fleetApi.orbitShip(shipSymbol);

        cache.updateIfExists(shipSymbol, ship -> {
            ship.setNav(orbitResponse.getData().getNav());
        });

        return orbitResponse;
    }

    @Override
    public GetShipNav200Response patchShipNav(String shipSymbol, PatchShipNavRequest patchShipNavRequest) {
        GetShipNav200Response response = fleetApi.patchShipNav(shipSymbol, patchShipNavRequest);

        cache.updateIfExists(shipSymbol, ship -> ship.setNav(response.getData().getNav()));

        return response;
    }

    @Override
    public Call<PurchaseCargo201Response> purchaseCargo(String shipSymbol, PurchaseCargoRequest purchaseCargoRequest) {
        return fleetApi.purchaseCargo(shipSymbol, purchaseCargoRequest);
    }

    @Override
    public Call<PurchaseShip201Response> purchaseShip(PurchaseShipRequest purchaseShipRequest) {
        return fleetApi.purchaseShip(purchaseShipRequest);
    }

    @Override
    public RefuelShip200Response refuelShip(String shipSymbol, RefuelShipRequest refuelShipRequest) {
        RefuelShip200Response refuelResponse = fleetApi.refuelShip(shipSymbol, refuelShipRequest);

        cache.updateIfExists(shipSymbol, ship -> {
            ship.setFuel(refuelResponse.getData().getFuel());
        });

        return refuelResponse;
    }

    @Override
    public Call<RemoveMount201Response> removeMount(String shipSymbol, RemoveMountRequest removeMountRequest) {
        return fleetApi.removeMount(shipSymbol, removeMountRequest);
    }

    @Override
    public SellCargo201Response sellCargo(String shipSymbol, SellCargoRequest sellCargoRequest) {
        return fleetApi.sellCargo(shipSymbol, sellCargoRequest);
    }

    @Override
    public Call<ShipRefine201Response> shipRefine(String shipSymbol, ShipRefineRequest shipRefineRequest) {
        return fleetApi.shipRefine(shipSymbol, shipRefineRequest);
    }

    @Override
    public Call<SiphonResources201Response> siphonResources(String shipSymbol) {
        return fleetApi.siphonResources(shipSymbol);
    }

    @Override
    public Call<TransferCargo200Response> transferCargo(String shipSymbol, TransferCargoRequest transferCargoRequest) {
        return fleetApi.transferCargo(shipSymbol, transferCargoRequest);
    }

    @Override
    public Call<NavigateShip200Response> warpShip(String shipSymbol, NavigateShipRequest navigateShipRequest) {
        return fleetApi.warpShip(shipSymbol, navigateShipRequest);
    }
}
