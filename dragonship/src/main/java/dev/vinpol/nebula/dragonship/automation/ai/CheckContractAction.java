package dev.vinpol.nebula.dragonship.automation.ai;

import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.IGoapUnit;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.models.Contract;
import dev.vinpol.spacetraders.sdk.models.FulfillContract200Response;

public class CheckContractAction extends GoapAction<AgentAI> {

    private final ContractsApi contractsApi;

    public CheckContractAction(AgentAI agentAI, ContractsApi contractsApi) {
        super(agentAI);

        this.contractsApi = contractsApi;
    }

    @Override
    protected boolean isDone(IGoapUnit goapUnit) {
        return true;
    }

    @Override
    protected boolean performAction(IGoapUnit goapUnit) {
        Contract contract = target.getContract();

        FulfillContract200Response response = contractsApi.fulfillContract(contract.getId());
        Contract updatedContract = response.getData().getContract();

        if (!updatedContract.getFulfilled()) {
            throw new IllegalStateException("Contract isn't fulfilled while our checked said that it should be fulfilled");
        }

        return true;
    }

    @Override
    protected float generateBaseCost(IGoapUnit goapUnit) {
        return 0;
    }

    @Override
    protected float generateCostRelativeToTarget(IGoapUnit goapUnit) {
        return 0;
    }

    @Override
    protected boolean requiresInRange(IGoapUnit goapUnit) {
        return false;
    }

    @Override
    protected boolean isInRange(IGoapUnit goapUnit) {
        return false;
    }

    @Override
    protected void reset() {

    }
}
