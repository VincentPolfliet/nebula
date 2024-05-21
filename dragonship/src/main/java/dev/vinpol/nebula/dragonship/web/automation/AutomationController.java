package dev.vinpol.nebula.dragonship.web.automation;

import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithm;
import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmDescription;
import dev.vinpol.nebula.dragonship.automation.algorithms.ShipAlgorithmResolver;
import dev.vinpol.nebula.dragonship.automation.behaviour.ShipBehaviour;
import dev.vinpol.nebula.dragonship.automation.command.ShipCommander;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/automation")
public class AutomationController {

    private final FleetApi fleetApi;
    private final ShipAlgorithmResolver shipAlgorithmResolver;
    private final ShipCommander commander;

    public AutomationController(FleetApi fleetApi, ShipAlgorithmResolver shipAlgorithmResolver, ShipCommander commander) {
        this.fleetApi = fleetApi;
        this.shipAlgorithmResolver = shipAlgorithmResolver;
        this.commander = commander;
    }

    @GetMapping("/{shipSymbol}")
    public String renderAutomation(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        Ship ship = fleetApi.getMyShip(shipSymbol).getData();
        model.addAttribute("ship", ship);

        ShipAlgorithm algorithm = shipAlgorithmResolver.resolve(ship.getRole());
        Objects.requireNonNull(algorithm);

        ShipAlgorithmDescription description = algorithm.description(ship);
        model.addAttribute("description", description);

        Optional<ShipBehaviour> currentBehaviourOptional = commander.getCurrentBehaviour(ship);
        currentBehaviourOptional.ifPresent(currentBehaviour -> {
            model.addAttribute("currentBehaviour", currentBehaviour);
        });

        model.addAttribute("possibleBehaviour", algorithm.decideBehaviour(ship).getName());

        return "automation/ship";
    }
}
