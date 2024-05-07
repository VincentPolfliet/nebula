package dev.vinpol.nebula.automation;

import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.nebula.javaGOAP.IGoapUnit;
import dev.vinpol.spacetraders.sdk.api.ContractsApi;
import dev.vinpol.spacetraders.sdk.models.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FindAContractAction extends GoapAction<AgentAI> {

    private final Logger logger = LoggerFactory.getLogger(FindAContractAction.class);

    private final ContractsApi contractsApi;

    public FindAContractAction(AgentAI target, ContractsApi contractsApi) {
        super(target);

        this.contractsApi = contractsApi;

        addEffect(new GoapState("HasContract", true));
        addPrecondition(new GoapState("HasContract", false));
    }

    @Override
    protected boolean isDone(IGoapUnit goapUnit) {
        return target.hasContract();
    }

    @Override
    protected boolean performAction(IGoapUnit goapUnit) {
        Optional<Contract> anyContract = contractsApi.streamContracts().findAny();

        if (anyContract.isEmpty()) {
            logger.warn("No contracts found while a contract is requested, this will (atm) cause an loop while searching for an available contract");
            return false;
        }

        Contract contract = anyContract.get();
        String contractId = contract.getId();
        logger.info("a contract '{}' has been found, accepting it", contractId);

        contractsApi.acceptContract(contractId);
        target.setContract(contract);
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
    protected boolean checkProceduralPrecondition(IGoapUnit goapUnit) {
        return true;
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
