package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShip200Response;
import dev.vinpol.spacetraders.sdk.models.Ship;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ShipController {

    private final FleetApi fleetApi;

    public ShipController(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @GetMapping("/ship/detail/{shipSymbol}")
    public String renderIndex(@PathVariable("shipSymbol") String shipSymbol, Model model) {
        GetMyShip200Response data = fleetApi.getMyShip(shipSymbol);
        Ship ship = data.getData();
        model.addAttribute("ship", ship);
        return "fleet/ship/index";
    }
}
