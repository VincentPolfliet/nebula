package dev.vinpol.spacetraders.sdk.api;

import dev.vinpol.spacetraders.sdk.models.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface SystemsApi {
    /**
     * Get Construction Site
     * Get construction details for a waypoint. Requires a waypoint with a property of &#x60;isUnderConstruction&#x60; to be true.
     *
     * @param systemSymbol   The system symbol (required)
     * @param waypointSymbol The waypoint symbol (required)
     * @return Call&lt;GetConstruction200Response&gt;
     */
    @GET("systems/{systemSymbol}/waypoints/{waypointSymbol}/construction")
    Call<GetConstruction200Response> getConstruction(
        @Path("systemSymbol") String systemSymbol, @Path("waypointSymbol") String waypointSymbol
    );

    /**
     * Get Jump Gate
     * Get jump gate details for a waypoint. Requires a waypoint of type &#x60;JUMP_GATE&#x60; to use.  Waypoints connected to this jump gate can be
     *
     * @param systemSymbol   The system symbol (required)
     * @param waypointSymbol The waypoint symbol (required)
     * @return Call&lt;GetJumpGate200Response&gt;
     */
    @GET("systems/{systemSymbol}/waypoints/{waypointSymbol}/jump-gate")
    Call<GetJumpGate200Response> getJumpGate(
        @Path("systemSymbol") String systemSymbol, @Path("waypointSymbol") String waypointSymbol
    );

    /**
     * Get Market
     * Retrieve imports, exports and exchange data from a marketplace. Requires a waypoint that has the &#x60;Marketplace&#x60; trait to use.  Send a ship to the waypoint to access trade good prices and recent transactions. Refer to the [Market Overview page](https://docs.spacetraders.io/game-concepts/markets) to gain better a understanding of the market in the game.
     *
     * @param systemSymbol   The system symbol (required)
     * @param waypointSymbol The waypoint symbol (required)
     * @return Call&lt;GetMarket200Response&gt;
     */
    @GET("systems/{systemSymbol}/waypoints/{waypointSymbol}/market")
    Call<GetMarket200Response> getMarket(
        @Path("systemSymbol") String systemSymbol, @Path("waypointSymbol") String waypointSymbol
    );

    /**
     * Get Shipyard
     * Get the shipyard for a waypoint. Requires a waypoint that has the &#x60;Shipyard&#x60; trait to use. Send a ship to the waypoint to access data on ships that are currently available for purchase and recent transactions.
     *
     * @param systemSymbol   The system symbol (required)
     * @param waypointSymbol The waypoint symbol (required)
     * @return Call&lt;GetShipyard200Response&gt;
     */
    @GET("systems/{systemSymbol}/waypoints/{waypointSymbol}/shipyard")
    Call<GetShipyard200Response> getShipyard(
        @Path("systemSymbol") String systemSymbol, @Path("waypointSymbol") String waypointSymbol
    );

    /**
     * Get System
     * Get the details of a system.
     *
     * @return Call&lt;GetSystem200Response&gt;
     */
    @GET("systems/{systemSymbol}")
    GetSystem200Response getSystem(@Path("systemSymbol") String systemSymbol);

    /**
     * List Waypoints in System
     * Return a paginated list of all of the waypoints for a given system.  If a waypoint is uncharted, it will return the &#x60;Uncharted&#x60; trait instead of its actual traits.
     *
     * @param systemSymbol The system symbol (required)
     * @param page         What entry offset to request (optional, default to 1)
     * @param limit        How many entries to return per page (optional, default to 10)
     * @param type         Filter waypoints by type. (optional)
     * @param traits       Filter waypoints by one or more traits. (optional)
     * @return Call&lt;GetSystemWaypoints200Response&gt;
     */
    @GET("systems/{systemSymbol}/waypoints")
    GetSystemWaypoints200Response getSystemWaypoints(@Path("systemSymbol") String systemSymbol, @Query("page") Integer page, @Query("limit") Integer limit, @Query("type") WaypointType type, @Query("traits") WaypointTraitSymbol... traits);

    /**
     * List Systems
     * Return a paginated list of all systems.
     *
     * @param page  What entry offset to request (optional, default to 1)
     * @param limit How many entries to return per page (optional, default to 10)
     * @return Call&lt;GetSystems200Response&gt;
     */
    @GET("systems")
    GetSystems200Response getSystems(@Query("page") Integer page, @Query("limit") Integer limit);

    /**
     * Get Waypoint
     * View the details of a waypoint.  If the waypoint is uncharted, it will return the &#39;Uncharted&#39; trait instead of its actual traits.
     *
     * @param systemSymbol   The system symbol (required)
     * @param waypointSymbol The waypoint symbol (required)
     * @return Call&lt;GetWaypoint200Response&gt;
     */
    @GET("systems/{systemSymbol}/waypoints/{waypointSymbol}")
    GetWaypoint200Response getWaypoint(@Path("systemSymbol") String systemSymbol, @Path("waypointSymbol") String waypointSymbol);

    /**
     * Supply Construction Site
     * Supply a construction site with the specified good. Requires a waypoint with a property of &#x60;isUnderConstruction&#x60; to be true.  The good must be in your ship&#39;s cargo. The good will be removed from your ship&#39;s cargo and added to the construction site&#39;s materials.
     *
     * @param systemSymbol              The system symbol (required)
     * @param waypointSymbol            The waypoint symbol (required)
     * @param supplyConstructionRequest (optional)
     * @return Call&lt;SupplyConstruction201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("systems/{systemSymbol}/waypoints/{waypointSymbol}/construction/supply")
    Call<SupplyConstruction201Response> supplyConstruction(
        @Path("systemSymbol") String systemSymbol, @Path("waypointSymbol") String waypointSymbol, @retrofit2.http.Body SupplyConstructionRequest supplyConstructionRequest
    );

}
