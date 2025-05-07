package dev.vinpol.spacetraders.sdk.api;

import dev.vinpol.spacetraders.sdk.models.GetStatus200Response;
import dev.vinpol.spacetraders.sdk.models.Register201Response;
import dev.vinpol.spacetraders.sdk.models.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DefaultApi {
    /**
     * Get Status
     * Return the status of the game server. This also includes a few global elements, such as announcements, server reset dates and leaderboards.
     *
     * @return Call&lt;GetStatus200Response&gt;
     */
    @GET("")
    Call<GetStatus200Response> getStatus();


    /**
     * Register New Agent
     * Creates a new agent and ties it to an account.  The agent symbol must consist of a 3-14 character string, and will be used to represent your agent. This symbol will prefix the symbol of every ship you own. Agent symbols will be cast to all uppercase characters.  This new agent will be tied to a starting faction of your choice, which determines your starting location, and will be granted an authorization token, a contract with their starting faction, a command ship that can fly across space with advanced capabilities, a small probe ship that can be used for reconnaissance, and 150,000 credits.  &gt; #### Keep your token safe and secure &gt; &gt; Save your token during the alpha phase. There is no way to regenerate this token without starting a new agent. In the future you will be able to generate and manage your tokens from the SpaceTraders website.  If you are new to SpaceTraders, It is recommended to register with the COSMIC faction, a faction that is well connected to the rest of the universe. After registering, you should try our interactive [quickstart guide](https://docs.spacetraders.io/quickstart/new-game) which will walk you through basic API requests in just a few minutes.
     *
     * @param registerRequest (optional)
     * @return Call&lt;Register201Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("register")
    Register201Response register(@Body RegisterRequest registerRequest);
}
