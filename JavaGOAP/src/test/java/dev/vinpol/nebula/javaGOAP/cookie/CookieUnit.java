package dev.vinpol.nebula.javaGOAP.cookie;

import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.nebula.javaGOAP.GoapUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;

/*
    WorldState: CookieInHand = false
    GoalState: EatingCookie = true

    AvailableActions:
	    TakeCookieInHand
		    - precondition: CookieInHand = false
		    - effect: CookieInHand = true
	    EatCookie
		    - precondition: CookieInHand = true
		    - effect: EatingCookie = true
		    - effect: CookieInHand = false

    Sequence:
	    TakeCookieInHand -> EatCookie
*/
public class CookieUnit extends GoapUnit {

    private final Logger logger = LoggerFactory.getLogger(CookieUnit.class);

    private final Cookie cookie;
    private final Hand hand;

    private final TakeCookieInHandAction takeCookie;
    private final EatCookieAction eatCookie;

    private Queue<GoapAction> currentPlan;
    private boolean isFinished;

    public CookieUnit(Cookie cookie, Hand hand) {
        this.cookie = cookie;
        this.hand = hand;

        this.takeCookie = new TakeCookieInHandAction(hand);
        this.eatCookie = new EatCookieAction(cookie, hand);

        addWorldState(new GoapState(Cookie.IN_HAND, false));
        addGoalState(new GoapState(Cookie.EAT, true));

        addAvailableAction(takeCookie);
        addAvailableAction(eatCookie);
    }

    @Override
    public void goapPlanFound(Queue<GoapAction> actions) {
        logger.info("goapPlanFound: {}", actions);
        this.currentPlan = actions;
    }

    @Override
    public void goapPlanFailed(Queue<GoapAction> actions) {
        logger.info("goapPlanFailed: {}", actions);
    }

    @Override
    public void goapPlanFinished() {
        logger.info("goapPlanFinished: {}", currentPlan);
        isFinished = true;
    }

    @Override
    public void update() {

    }

    @Override
    public boolean moveTo(Object target) {
        return false;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Queue<GoapAction> getCurrentPlan() {
        return currentPlan;
    }
}
