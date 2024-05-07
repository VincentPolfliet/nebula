package dev.vinpol.spacetraders.sdk.api;

import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import retrofit2.Call;
import retrofit2.http.*;

public interface FleetApi {
    /**
     * Create Chart
     * Command a ship to chart the waypoint at its current location.  Most waypoints in the universe are uncharted by default. These waypoints have their traits hidden until they have been charted by a ship.  Charting a waypoint will record your agent as the one who created the chart, and all other agents would also be able to see the waypoint&#39;s traits.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;CreateChart201Response&gt;
     */
    @POST("my/ships/{shipSymbol}/chart")
    Call<CreateChart201Response> createChart(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Scan Ships
     * Scan for nearby ships, retrieving information for all ships in range.  Requires a ship to have the &#x60;Sensor Array&#x60; mount installed to use.  The ship will enter a cooldown after using this function, during which it cannot execute certain actions.
     *
     * @param shipSymbol The ship symbol. (required)
     * @return Call&lt;CreateShipShipScan201Response&gt;
     */
    @POST("my/ships/{shipSymbol}/scan/ships")
    Call<CreateShipShipScan201Response> createShipShipScan(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Scan Systems
     * Scan for nearby systems, retrieving information on the systems&#39; distance from the ship and their waypoints. Requires a ship to have the &#x60;Sensor Array&#x60; mount installed to use.  The ship will enter a cooldown after using this function, during which it cannot execute certain actions.
     *
     * @param shipSymbol The ship symbol. (required)
     * @return Call&lt;CreateShipSystemScan201Response&gt;
     */
    @POST("my/ships/{shipSymbol}/scan/systems")
    Call<CreateShipSystemScan201Response> createShipSystemScan(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Scan Waypoints
     * Scan for nearby waypoints, retrieving detailed information on each waypoint in range. Scanning uncharted waypoints will allow you to ignore their uncharted state and will list the waypoints&#39; traits.  Requires a ship to have the &#x60;Sensor Array&#x60; mount installed to use.  The ship will enter a cooldown after using this function, during which it cannot execute certain actions.
     *
     * @param shipSymbol The ship symbol. (required)
     * @return Call&lt;CreateShipWaypointScan201Response&gt;
     */
    @POST("my/ships/{shipSymbol}/scan/waypoints")
    Call<CreateShipWaypointScan201Response> createShipWaypointScan(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Create Survey
     * Create surveys on a waypoint that can be extracted such as asteroid fields. A survey focuses on specific types of deposits from the extracted location. When ships extract using this survey, they are guaranteed to procure a high amount of one of the goods in the survey.  In order to use a survey, send the entire survey details in the body of the extract request.  Each survey may have multiple deposits, and if a symbol shows up more than once, that indicates a higher chance of extracting that resource.  Your ship will enter a cooldown after surveying in which it is unable to perform certain actions. Surveys will eventually expire after a period of time or will be exhausted after being extracted several times based on the survey&#39;s size. Multiple ships can use the same survey for extraction.  A ship must have the &#x60;Surveyor&#x60; mount installed in order to use this function.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;CreateSurvey201Response&gt;
     */
    @POST("my/ships/{shipSymbol}/survey")
    Call<CreateSurvey201Response> createSurvey(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Dock Ship
     * Attempt to dock your ship at its current location. Docking will only succeed if your ship is capable of docking at the time of the request.  Docked ships can access elements in their current location, such as the market or a shipyard, but cannot do actions that require the ship to be above surface such as navigating or extracting.  The endpoint is idempotent - successive calls will succeed even if the ship is already docked.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;DockShip200Response&gt;
     */
    @POST("my/ships/{shipSymbol}/dock")
    DockShip200Response dockShip(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Extract Resources
     * Extract resources from a waypoint that can be extracted, such as asteroid fields, into your ship. Send an optional survey as the payload to target specific yields.  The ship must be in orbit to be able to extract and must have mining equipments installed that can extract goods, such as the &#x60;Gas Siphon&#x60; mount for gas-based goods or &#x60;Mining Laser&#x60; mount for ore-based goods.  The survey property is now deprecated. See the &#x60;extract/survey&#x60; endpoint for more details.
     *
     * @param shipSymbol              The ship symbol. (required)
     * @param extractResourcesRequest (optional)
     * @return Call&lt;ExtractResources201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/extract")
    ExtractResources201Response extractResources(
        @Path("shipSymbol") String shipSymbol, @Body ExtractResourcesRequest extractResourcesRequest
    );

    /**
     * Extract Resources with Survey
     * Use a survey when extracting resources from a waypoint. This endpoint requires a survey as the payload, which allows your ship to extract specific yields.  Send the full survey object as the payload which will be validated according to the signature. If the signature is invalid, or any properties of the survey are changed, the request will fail.
     *
     * @param shipSymbol The ship symbol. (required)
     * @param survey     (optional)
     * @return Call&lt;ExtractResources201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/extract/survey")
    Call<ExtractResources201Response> extractResourcesWithSurvey(
        @Path("shipSymbol") String shipSymbol, @Body Survey survey
    );

    /**
     * Get Mounts
     * Get the mounts installed on a ship.
     *
     * @param shipSymbol The ship&#39;s symbol. (required)
     * @return Call&lt;GetMounts200Response&gt;
     */
    @GET("my/ships/{shipSymbol}/mounts")
    Call<GetMounts200Response> getMounts(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Get Ship
     * Retrieve the details of a ship under your agent&#39;s ownership.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;GetMyShip200Response&gt;
     */
    @GET("my/ships/{shipSymbol}")
    Call<GetMyShip200Response> getMyShip(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Get Ship Cargo
     * Retrieve the cargo of a ship under your agent&#39;s ownership.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;GetMyShipCargo200Response&gt;
     */
    @GET("my/ships/{shipSymbol}/cargo")
    Call<GetMyShipCargo200Response> getMyShipCargo(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * List Ships
     * Return a paginated list of all of ships under your agent&#39;s ownership.
     *
     * @param page  What entry offset to request (optional, default to 1)
     * @param limit How many entries to return per page (optional, default to 10)
     * @return Call&lt;GetMyShips200Response&gt;
     */
    @GET("my/ships")
    GetMyShips200Response getMyShips(
        @Query("page") Integer page, @Query("limit") Integer limit
    );

    default Iterable<Ship> getMyShips() {
        return PageIterator.iterate(this::getMyShips);
    }

    /**
     * Get Ship Cooldown
     * Retrieve the details of your ship&#39;s reactor cooldown. Some actions such as activating your jump drive, scanning, or extracting resources taxes your reactor and results in a cooldown.  Your ship cannot perform additional actions until your cooldown has expired. The duration of your cooldown is relative to the power consumption of the related modules or mounts for the action taken.  Response returns a 204 status code (no-content) when the ship has no cooldown.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;GetShipCooldown200Response&gt;
     */
    @GET("my/ships/{shipSymbol}/cooldown")
    Call<GetShipCooldown200Response> getShipCooldown(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Get Ship Nav
     * Get the current nav status of a ship.
     *
     * @param shipSymbol The ship symbol. (required)
     * @return Call&lt;GetShipNav200Response&gt;
     */
    @GET("my/ships/{shipSymbol}/nav")
    Call<GetShipNav200Response> getShipNav(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Install Mount
     * Install a mount on a ship.  In order to install a mount, the ship must be docked and located in a waypoint that has a &#x60;Shipyard&#x60; trait. The ship also must have the mount to install in its cargo hold.  An installation fee will be deduced by the Shipyard for installing the mount on the ship.
     *
     * @param shipSymbol          The ship&#39;s symbol. (required)
     * @param installMountRequest (optional)
     * @return Call&lt;InstallMount201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/mounts/install")
    Call<InstallMount201Response> installMount(
        @Path("shipSymbol") String shipSymbol, @Body InstallMountRequest installMountRequest
    );

    /**
     * Jettison Cargo
     * Jettison cargo from your ship&#39;s cargo hold.
     *
     * @param shipSymbol      The ship symbol. (required)
     * @param jettisonRequest (optional)
     * @return Call&lt;Jettison200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/jettison")
    Call<Jettison200Response> jettison(
        @Path("shipSymbol") String shipSymbol, @Body JettisonRequest jettisonRequest
    );

    /**
     * Jump Ship
     * Jump your ship instantly to a target connected waypoint. The ship must be in orbit to execute a jump.  A unit of antimatter is purchased and consumed from the market when jumping. The price of antimatter is determined by the market and is subject to change. A ship can only jump to connected waypoints
     *
     * @param shipSymbol      The ship symbol. (required)
     * @param jumpShipRequest (optional)
     * @return Call&lt;JumpShip200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/jump")
    Call<JumpShip200Response> jumpShip(
        @Path("shipSymbol") String shipSymbol, @Body JumpShipRequest jumpShipRequest
    );

    /**
     * Navigate Ship
     * Navigate to a target destination. The ship must be in orbit to use this function. The destination waypoint must be within the same system as the ship&#39;s current location. Navigating will consume the necessary fuel from the ship&#39;s manifest based on the distance to the target waypoint.  The returned response will detail the route information including the expected time of arrival. Most ship actions are unavailable until the ship has arrived at it&#39;s destination.  To travel between systems, see the ship&#39;s Warp or Jump actions.
     *
     * @param shipSymbol          The ship symbol. (required)
     * @param navigateShipRequest (optional)
     * @return Call&lt;NavigateShip200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/navigate")
    NavigateShip200Response navigateShip(@Path("shipSymbol") String shipSymbol, @Body NavigateShipRequest navigateShipRequest);

    /**
     * Negotiate Contract
     * Negotiate a new contract with the HQ.  In order to negotiate a new contract, an agent must not have ongoing or offered contracts over the allowed maximum amount. Currently the maximum contracts an agent can have at a time is 1.  Once a contract is negotiated, it is added to the list of contracts offered to the agent, which the agent can then accept.   The ship must be present at any waypoint with a faction present to negotiate a contract with that faction.
     *
     * @param shipSymbol The ship&#39;s symbol. (required)
     * @return Call&lt;NegotiateContract200Response&gt;
     */
    @POST("my/ships/{shipSymbol}/negotiate/contract")
    Call<NegotiateContract200Response> negotiateContract(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Orbit Ship
     * Attempt to move your ship into orbit at its current location. The request will only succeed if your ship is capable of moving into orbit at the time of the request.  Orbiting ships are able to do actions that require the ship to be above surface such as navigating or extracting, but cannot access elements in their current waypoint, such as the market or a shipyard.  The endpoint is idempotent - successive calls will succeed even if the ship is already in orbit.
     *
     * @param shipSymbol The symbol of the ship. (required)
     * @return Call&lt;OrbitShip200Response&gt;
     */
    @POST("my/ships/{shipSymbol}/orbit")
    OrbitShip200Response orbitShip(@Path("shipSymbol") String shipSymbol);

    /**
     * Patch Ship Nav
     * Update the nav configuration of a ship.  Currently only supports configuring the Flight Mode of the ship, which affects its speed and fuel consumption.
     *
     * @param shipSymbol          The ship symbol. (required)
     * @param patchShipNavRequest (optional)
     * @return Call&lt;GetShipNav200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @PATCH("my/ships/{shipSymbol}/nav")
    Call<GetShipNav200Response> patchShipNav(
        @Path("shipSymbol") String shipSymbol, @Body PatchShipNavRequest patchShipNavRequest
    );

    /**
     * Purchase Cargo
     * Purchase cargo from a market.  The ship must be docked in a waypoint that has &#x60;Marketplace&#x60; trait, and the market must be selling a good to be able to purchase it.  The maximum amount of units of a good that can be purchased in each transaction are denoted by the &#x60;tradeVolume&#x60; value of the good, which can be viewed by using the Get Market action.  Purchased goods are added to the ship&#39;s cargo hold.
     *
     * @param shipSymbol           The ship&#39;s symbol. (required)
     * @param purchaseCargoRequest (optional)
     * @return Call&lt;PurchaseCargo201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/purchase")
    Call<PurchaseCargo201Response> purchaseCargo(
        @Path("shipSymbol") String shipSymbol, @Body PurchaseCargoRequest purchaseCargoRequest
    );

    /**
     * Purchase Ship
     * Purchase a ship from a Shipyard. In order to use this function, a ship under your agent&#39;s ownership must be in a waypoint that has the &#x60;Shipyard&#x60; trait, and the Shipyard must sell the type of the desired ship.  Shipyards typically offer ship types, which are predefined templates of ships that have dedicated roles. A template comes with a preset of an engine, a reactor, and a frame. It may also include a few modules and mounts.
     *
     * @param purchaseShipRequest (optional)
     * @return Call&lt;PurchaseShip201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships")
    Call<PurchaseShip201Response> purchaseShip(
        @Body PurchaseShipRequest purchaseShipRequest
    );

    /**
     * Refuel Ship
     * Refuel your ship by buying fuel from the local market.  Requires the ship to be docked in a waypoint that has the &#x60;Marketplace&#x60; trait, and the market must be selling fuel in order to refuel.  Each fuel bought from the market replenishes 100 units in your ship&#39;s fuel.  Ships will always be refuel to their frame&#39;s maximum fuel capacity when using this action.
     *
     * @param shipSymbol        The ship symbol. (required)
     * @param refuelShipRequest (optional)
     * @return Call&lt;RefuelShip200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/refuel")
    RefuelShip200Response refuelShip(
        @Path("shipSymbol") String shipSymbol, @Body RefuelShipRequest refuelShipRequest
    );

    /**
     * Remove Mount
     * Remove a mount from a ship.  The ship must be docked in a waypoint that has the &#x60;Shipyard&#x60; trait, and must have the desired mount that it wish to remove installed.  A removal fee will be deduced from the agent by the Shipyard.
     *
     * @param shipSymbol         The ship&#39;s symbol. (required)
     * @param removeMountRequest (optional)
     * @return Call&lt;RemoveMount201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/mounts/remove")
    Call<RemoveMount201Response> removeMount(
        @Path("shipSymbol") String shipSymbol, @Body RemoveMountRequest removeMountRequest
    );

    /**
     * Sell Cargo
     * Sell cargo in your ship to a market that trades this cargo. The ship must be docked in a waypoint that has the &#x60;Marketplace&#x60; trait in order to use this function.
     *
     * @param shipSymbol       Symbol of a ship. (required)
     * @param sellCargoRequest (optional)
     * @return Call&lt;SellCargo201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/sell")
    Call<SellCargo201Response> sellCargo(
        @Path("shipSymbol") String shipSymbol, @Body SellCargoRequest sellCargoRequest
    );

    /**
     * Ship Refine
     * Attempt to refine the raw materials on your ship. The request will only succeed if your ship is capable of refining at the time of the request. In order to be able to refine, a ship must have goods that can be refined and have installed a &#x60;Refinery&#x60; module that can refine it.  When refining, 30 basic goods will be converted into 10 processed goods.
     *
     * @param shipSymbol        The symbol of the ship. (required)
     * @param shipRefineRequest (optional)
     * @return Call&lt;ShipRefine201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/refine")
    Call<ShipRefine201Response> shipRefine(
        @Path("shipSymbol") String shipSymbol, @Body ShipRefineRequest shipRefineRequest
    );

    /**
     * Siphon Resources
     * Siphon gases, such as hydrocarbon, from gas giants.  The ship must be in orbit to be able to siphon and must have siphon mounts and a gas processor installed.
     *
     * @param shipSymbol The ship symbol. (required)
     * @return Call&lt;SiphonResources201Response&gt;
     */
    @POST("my/ships/{shipSymbol}/siphon")
    Call<SiphonResources201Response> siphonResources(
        @Path("shipSymbol") String shipSymbol
    );

    /**
     * Transfer Cargo
     * Transfer cargo between ships.  The receiving ship must be in the same waypoint as the transferring ship, and it must able to hold the additional cargo after the transfer is complete. Both ships also must be in the same state, either both are docked or both are orbiting.  The response body&#39;s cargo shows the cargo of the transferring ship after the transfer is complete.
     *
     * @param shipSymbol           The transferring ship&#39;s symbol. (required)
     * @param transferCargoRequest (optional)
     * @return Call&lt;TransferCargo200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/transfer")
    Call<TransferCargo200Response> transferCargo(
        @Path("shipSymbol") String shipSymbol, @Body TransferCargoRequest transferCargoRequest
    );

    /**
     * Warp Ship
     * Warp your ship to a target destination in another system. The ship must be in orbit to use this function and must have the &#x60;Warp Drive&#x60; module installed. Warping will consume the necessary fuel from the ship&#39;s manifest.  The returned response will detail the route information including the expected time of arrival. Most ship actions are unavailable until the ship has arrived at its destination.
     *
     * @param shipSymbol          The ship symbol. (required)
     * @param navigateShipRequest (optional)
     * @return Call&lt;NavigateShip200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/ships/{shipSymbol}/warp")
    Call<NavigateShip200Response> warpShip(
        @Path("shipSymbol") String shipSymbol, @Body NavigateShipRequest navigateShipRequest
    );

}
