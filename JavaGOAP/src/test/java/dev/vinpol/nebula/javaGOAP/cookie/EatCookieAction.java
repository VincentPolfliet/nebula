package dev.vinpol.nebula.javaGOAP.cookie;

import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.nebula.javaGOAP.IGoapUnit;

public class EatCookieAction extends GoapAction {

    private final Hand hand;

    public EatCookieAction(Cookie target, Hand hand) {
        super(target);
        this.hand = hand;

        addPrecondition(new GoapState(Cookie.IN_HAND, true));

        addEffect(new GoapState(Cookie.EAT, true));
        addEffect(new GoapState(Cookie.IN_HAND, false));
    }

    @Override
    public boolean isDone(IGoapUnit goapUnit) {
        return ((Cookie) target).isEaten();
    }

    @Override
    public boolean performAction(IGoapUnit goapUnit) {
        ((Cookie) target).eat();
        hand.setCookieInHand(false);
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
