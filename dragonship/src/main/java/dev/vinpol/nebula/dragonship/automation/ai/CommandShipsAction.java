package dev.vinpol.nebula.dragonship.automation.ai;

import dev.vinpol.nebula.dragonship.automation.command.ShipCommander;
import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.nebula.javaGOAP.IGoapUnit;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShips200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.utils.page.Page;
import dev.vinpol.spacetraders.sdk.utils.page.PageIterator;

public class CommandShipsAction extends GoapAction<AgentAI> {
    private final FleetApi fleetApi;
    private final ShipCommander shipCommander;

    public CommandShipsAction(AgentAI agentAI, FleetApi fleetApi, ShipCommander shipCommander) {
        super(agentAI);

        addPrecondition(new GoapState("HasContract", true));
        addEffect(new GoapState("CheckContract", true));

        this.fleetApi = fleetApi;
        this.shipCommander = shipCommander;
    }

    @Override
    protected boolean isDone(IGoapUnit goapUnit) {
        return true;
    }

    @Override
    protected boolean performAction(IGoapUnit goapUnit) {
        Iterable<Ship> shipIterable = PageIterator.iterate(req -> {
            GetMyShips200Response response = fleetApi.getMyShips(req.page(), req.size());

            return new Page<>(
                    response.getData(),
                    response.getMeta().getTotal()
            );
        });

        for (Ship ship : shipIterable) {
            shipCommander.command(ship);
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
