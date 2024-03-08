package dev.vinpol.spacetraders.sdk.api;

import retrofit2.Call;
import retrofit2.http.*;

import dev.vinpol.spacetraders.sdk.models.AcceptContract200Response;
import dev.vinpol.spacetraders.sdk.models.DeliverContract200Response;
import dev.vinpol.spacetraders.sdk.models.DeliverContractRequest;
import dev.vinpol.spacetraders.sdk.models.FulfillContract200Response;
import dev.vinpol.spacetraders.sdk.models.GetContract200Response;
import dev.vinpol.spacetraders.sdk.models.GetContracts200Response;

public interface ContractsApi {
  /**
   * Accept Contract
   * Accept a contract by ID.   You can only accept contracts that were offered to you, were not accepted yet, and whose deadlines has not passed yet.
   * @param contractId The contract ID to accept. (required)
   * @return Call&lt;AcceptContract200Response&gt;
   */
  @POST("my/contracts/{contractId}/accept")
  Call<AcceptContract200Response> acceptContract(
    @retrofit2.http.Path("contractId") String contractId
  );

  /**
   * Deliver Cargo to Contract
   * Deliver cargo to a contract.  In order to use this API, a ship must be at the delivery location (denoted in the delivery terms as &#x60;destinationSymbol&#x60; of a contract) and must have a number of units of a good required by this contract in its cargo.  Cargo that was delivered will be removed from the ship&#39;s cargo.
   * @param contractId The ID of the contract. (required)
   * @param deliverContractRequest  (optional)
   * @return Call&lt;DeliverContract200Response&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("my/contracts/{contractId}/deliver")
  Call<DeliverContract200Response> deliverContract(
    @retrofit2.http.Path("contractId") String contractId, @retrofit2.http.Body DeliverContractRequest deliverContractRequest
  );

  /**
   * Fulfill Contract
   * Fulfill a contract. Can only be used on contracts that have all of their delivery terms fulfilled.
   * @param contractId The ID of the contract to fulfill. (required)
   * @return Call&lt;FulfillContract200Response&gt;
   */
  @POST("my/contracts/{contractId}/fulfill")
  Call<FulfillContract200Response> fulfillContract(
    @retrofit2.http.Path("contractId") String contractId
  );

  /**
   * Get Contract
   * Get the details of a contract by ID.
   * @param contractId The contract ID (required)
   * @return Call&lt;GetContract200Response&gt;
   */
  @GET("my/contracts/{contractId}")
  Call<GetContract200Response> getContract(
    @retrofit2.http.Path("contractId") String contractId
  );

  /**
   * List Contracts
   * Return a paginated list of all your contracts.
   * @param page What entry offset to request (optional, default to 1)
   * @param limit How many entries to return per page (optional, default to 10)
   * @return Call&lt;GetContracts200Response&gt;
   */
  @GET("my/contracts")
  Call<GetContracts200Response> getContracts(
    @retrofit2.http.Query("page") Integer page, @retrofit2.http.Query("limit") Integer limit
  );

}
