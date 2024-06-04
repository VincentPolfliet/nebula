package dev.vinpol.spacetraders.sdk.api;

import dev.vinpol.spacetraders.sdk.models.*;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.stream.Stream;

@RateLimited
public interface ContractsApi {
    /**
     * Accept Contract
     * Accept a contract by ID.   You can only accept contracts that were offered to you, were not accepted yet, and whose deadlines has not passed yet.
     *
     * @param contractId The contract ID to accept. (required)
     * @return Call&lt;AcceptContract200Response&gt;
     */
    @POST("my/contracts/{contractId}/accept")
    AcceptContract200Response acceptContract(@Path("contractId") String contractId);

    /**
     * Deliver Cargo to Contract
     * Deliver cargo to a contract.  In order to use this API, a ship must be at the delivery location (denoted in the delivery terms as &#x60;destinationSymbol&#x60; of a contract) and must have a number of units of a good required by this contract in its cargo.  Cargo that was delivered will be removed from the ship&#39;s cargo.
     *
     * @param contractId             The ID of the contract. (required)
     * @param deliverContractRequest (optional)
     * @return Call&lt;DeliverContract200Response&gt;
     */
    @Headers({
        "Content-Type:application/json"
    })
    @POST("my/contracts/{contractId}/deliver")
    Call<DeliverContract200Response> deliverContract(
        @Path("contractId") String contractId, @retrofit2.http.Body DeliverContractRequest deliverContractRequest
    );

    /**
     * Fulfill Contract
     * Fulfill a contract. Can only be used on contracts that have all of their delivery terms fulfilled.
     *
     * @param contractId The ID of the contract to fulfill. (required)
     * @return Call&lt;FulfillContract200Response&gt;
     */
    @POST("my/contracts/{contractId}/fulfill")
    FulfillContract200Response fulfillContract(
        @Path("contractId") String contractId
    );

    /**
     * Get Contract
     * Get the details of a contract by ID.
     *
     * @param contractId The contract ID (required)
     * @return Call&lt;GetContract200Response&gt;
     */
    @GET("my/contracts/{contractId}")
    Call<GetContract200Response> getContract(
        @Path("contractId") String contractId
    );

    /**
     * List Contracts
     * Return a paginated list of all your contracts.
     *
     * @param page  What entry offset to request (optional, default to 1)
     * @param limit How many entries to return per page (optional, default to 10)
     * @return Call&lt;GetContracts200Response&gt;
     */
    @GET("my/contracts")
    GetContracts200Response getContracts(@Query("page") Integer page, @Query("limit") Integer limit);

    default Stream<Contract> streamContracts() {
        return PageIterator.stream(req -> {
            GetContracts200Response response = getContracts(req.page(), req.size());

            return new Page<>(
                response.getData(),
                response.getMeta().getTotal()
            );
        });
    }
}
