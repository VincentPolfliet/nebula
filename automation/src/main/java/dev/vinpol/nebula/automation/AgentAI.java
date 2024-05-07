package dev.vinpol.nebula.automation;

import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.nebula.javaGOAP.GoapUnit;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.models.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;


public class AgentAI extends GoapUnit {

    private final Logger logger = LoggerFactory.getLogger(AgentAI.class);

    private final FindAContractAction findAContractAction;
    private final CommandShipsAction commandShipsAction;
    private final CheckContractAction checkContractAction;

    private Contract contract;

    public AgentAI(ApiClient apiClient, ShipCommander shipCommander) {
        addWorldState(new GoapState("HasContract", false));
        addGoalState(new GoapState("HasContract", true));

        findAContractAction = new FindAContractAction(this, apiClient.contractsApi());
        commandShipsAction = new CommandShipsAction(this, apiClient.fleetApi(), shipCommander);
        checkContractAction = new CheckContractAction(this, apiClient.contractsApi());

        addAvailableAction(findAContractAction);
        addAvailableAction(commandShipsAction);
        addAvailableAction(checkContractAction);
    }

    @Override
    public void goapPlanFound(Queue<GoapAction> actions) {
        logger.info("Goap Plan Found: {}", actions);
    }

    @Override
    public void goapPlanFailed(Queue<GoapAction> actions) {
        logger.info("Goap Plan Failed: {}", actions);
    }

    @Override
    public void goapPlanFinished() {
        logger.info("Goap Plan Finished");
    }

    @Override
    public void update() {
    }

    @Override
    public boolean moveTo(Object target) {
        return false;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;

        removeWorldState("HasContract");
        addWorldState(new GoapState("HasContract", contract != null));
    }

    public boolean hasContract() {
        return contract != null;
    }
}
