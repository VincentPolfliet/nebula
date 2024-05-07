package dev.vinpol.nebula.javaGOAP.cookie;

import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.nebula.javaGOAP.IGoapUnit;

public class TakeCookieInHandAction extends GoapAction {

    public TakeCookieInHandAction(Hand target) {
        super(target);

        this.target = target;

        addEffect(new GoapState("cookieInHand", true));
        addPrecondition(new GoapState("cookieInHand", false));
    }

    @Override
    public boolean isDone(IGoapUnit goapUnit) {
        return ((Hand) target).isCookieInHand();
    }

    @Override
    public boolean performAction(IGoapUnit goapUnit) {
        ((Hand) target).setCookieInHand();
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
