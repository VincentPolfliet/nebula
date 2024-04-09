package dev.vinpol.nebula.dragonship.web.ships;

import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShips200Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FleetController {

    private final FleetApi fleetApi;

    public FleetController(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @GetMapping("/fleet")
    public String getFleet(@RequestParam(value = "page", defaultValue = "1") int page, @RequestParam(value = "total", defaultValue = "1") int total) {
        GetMyShips200Response response = fleetApi.getMyShips(page, total);

        return "fleet";
    }
}
