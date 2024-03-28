package dev.vinpol.spacetraders.sdk.api;

import dev.vinpol.spacetraders.sdk.models.GetAgents200Response;
import dev.vinpol.spacetraders.sdk.models.GetMyAgent200Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AgentsApi {
    /**
     * Get Public Agent
     * Fetch agent details.
     *
     * @param agentSymbol The agent symbol (required)
     * @return Call&lt;GetMyAgent200Response&gt;
     */
    @GET("agents/{agentSymbol}")
    Call<GetMyAgent200Response> getAgent(@Path("agentSymbol") String agentSymbol);

    /**
     * List Agents
     * Fetch agents details.
     *
     * @param page  What entry offset to request (optional, default to 1)
     * @param limit How many entries to return per page (optional, default to 10)
     * @return Call&lt;GetAgents200Response&gt;
     */
    @GET("agents")
    Call<GetAgents200Response> getAgents(@Query("page") Integer page, @Query("limit") Integer limit);

    /**
     * Get Agent
     * Fetch your agent&#39;s details.
     *
     * @return Call&lt;GetMyAgent200Response&gt;
     */
    @GET("my/agent")
    Call<GetMyAgent200Response> getMyAgent();
}
