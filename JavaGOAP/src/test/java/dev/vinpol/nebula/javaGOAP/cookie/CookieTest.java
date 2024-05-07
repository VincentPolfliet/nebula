package dev.vinpol.nebula.javaGOAP.cookie;

import dev.vinpol.nebula.javaGOAP.DefaultGoapAgent;
import dev.vinpol.nebula.javaGOAP.GoapAction;
import dev.vinpol.nebula.javaGOAP.GoapState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieTest {

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

    private Cookie cookie;
    private Hand hand;
    private CookieUnit cookieUnit;

    @BeforeEach
    void setUp() {
        cookie = new Cookie();
        hand = new Hand();
        cookieUnit = new CookieUnit(cookie, hand);
    }

    @Test
    void invoke() {
        DefaultGoapAgent agent = new DefaultGoapAgent(cookieUnit);
        // lookup plan
        agent.update();

        // take cookie in hand
        agent.update();
        assertThat(hand.isCookieInHand()).isTrue();

        // eat the cookie
        agent.update();

        assertThat(cookie.isEaten()).isTrue();
        assertThat(hand.isCookieInHand()).isFalse();

        agent.update();

        assertThat(cookieUnit.isFinished()).isTrue();
    }

    @Test
    void actions() {
        Set<GoapAction> actions = cookieUnit.getAvailableActions();

        assertThat(actions).hasSize(2);

        assertThat(actions)
            .hasOnlyElementsOfTypes(EatCookieAction.class, TakeCookieInHandAction.class);
    }

    @Test
    void goalState() {
        List<GoapState> goalState = cookieUnit.getGoalState();

        assertThat(goalState).hasSize(1);
        assertThat(goalState)
            .first()
            .satisfies(state -> {
                assertThat(state.effect).isEqualTo("eatingCookie");
                assertThat(state.value).isEqualTo(true);
            });
    }

    @Test
    void worldState() {
        Set<GoapState> worldState = cookieUnit.getWorldState();

        assertThat(worldState).hasSize(1);
        assertThat(worldState).first()
            .satisfies(state -> {
                assertThat(state.effect).isEqualTo("cookieInHand");
                assertThat(state.value).isEqualTo(false);
            });
    }
}
