package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.nebula.dragonship.automation.ai.AgentAI;
import dev.vinpol.nebula.dragonship.automation.command.ShipCommander;
import dev.vinpol.nebula.dragonship.sdk.ApiClientStub;
import dev.vinpol.nebula.javaGOAP.DefaultGoapAgent;
import dev.vinpol.nebula.javaGOAP.GoapAgent;
import dev.vinpol.nebula.javaGOAP.GoapState;
import dev.vinpol.spacetraders.sdk.ApiClient;
import dev.vinpol.spacetraders.sdk.models.Contract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AgentAITest {

    private ApiClient apiClient;
    private ShipCommander shipCommander;

    @BeforeEach
    void setUp() {
        apiClient = new ApiClientStub();
        shipCommander = Mockito.mock(ShipCommander.class);
    }

    @Test
    void lookingUpContract() {
        AgentAI ai = spy(new AgentAI(apiClient, shipCommander));

        ai.setContract(
            new Contract()
                .id("IM-FAST-&-IM-FURIOUS")
                .accepted(true)
        );

        GoapAgent agent = new DefaultGoapAgent(ai);

        agent.update();

        assertThat(ai.getContract().getId()).isEqualTo("IM-FAST-&-IM-FURIOUS");
        Set<GoapState> worldState = ai.getWorldState();

        assertThat(worldState).containsOnly(new GoapState("HasContract", true));
    }

    @Test
    void lookingUpContract2() {
        var contractStream = Stream.of(
            new Contract()
                .id("ID-TO-LOOKUP")
        );

        when(apiClient.contractsApi().streamContracts()).thenReturn(contractStream);

        AgentAI ai = new AgentAI(apiClient, shipCommander);

        GoapAgent agent = new DefaultGoapAgent(ai);

        // lookup plan
        agent.update();

        // looking up contract
        agent.update();

        assertThat(ai.getContract().getId()).isEqualTo("ID-TO-LOOKUP");

        Set<GoapState> worldState = ai.getWorldState();
        assertThat(worldState).containsOnly(new GoapState("HasContract", true));
    }
}
