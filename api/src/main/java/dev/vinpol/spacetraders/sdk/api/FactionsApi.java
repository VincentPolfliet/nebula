package dev.vinpol.spacetraders.sdk.api;

import retrofit2.Call;
import retrofit2.http.*;

import dev.vinpol.spacetraders.sdk.models.GetFaction200Response;
import dev.vinpol.spacetraders.sdk.models.GetFactions200Response;

public interface FactionsApi {
  /**
   * Get Faction
   * View the details of a faction.
   * @param factionSymbol The faction symbol (required)
   * @return Call&lt;GetFaction200Response&gt;
   */
  @GET("factions/{factionSymbol}")
  Call<GetFaction200Response> getFaction(
    @retrofit2.http.Path("factionSymbol") String factionSymbol
  );

  /**
   * List Factions
   * Return a paginated list of all the factions in the game.
   * @param page What entry offset to request (optional, default to 1)
   * @param limit How many entries to return per page (optional, default to 10)
   * @return Call&lt;GetFactions200Response&gt;
   */
  @GET("factions")
  Call<GetFactions200Response> getFactions(
    @retrofit2.http.Query("page") Integer page, @retrofit2.http.Query("limit") Integer limit
  );

}
