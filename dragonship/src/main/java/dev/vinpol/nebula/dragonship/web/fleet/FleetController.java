package dev.vinpol.nebula.dragonship.web.fleet;

import dev.vinpol.nebula.dragonship.web.Page;
import dev.vinpol.spacetraders.sdk.api.FleetApi;
import dev.vinpol.spacetraders.sdk.models.GetMyShips200Response;
import dev.vinpol.spacetraders.sdk.utils.page.PageRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FleetController {

    private final FleetApi fleetApi;

    public FleetController(FleetApi fleetApi) {
        this.fleetApi = fleetApi;
    }

    @GetMapping(value = "/fleet")
    public String getFleet(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "total", defaultValue = "10") int total,
                           Model model) {

        Page contentPage = new Page();
        contentPage.setTitle("Fleet");

        model.addAttribute("page", contentPage);
        model.addAttribute("pageRequest", new PageRequest(page, total));

        return "fleet/index";
    }

    @GetMapping(value = "/fleet.htmx")
    public String getFleetData(@RequestParam(value = "page", defaultValue = "1") int page,
                               @RequestParam(value = "total", defaultValue = "10") int total,
                               HttpServletRequest request,
                               Model model) {


        GetMyShips200Response response = fleetApi.getMyShips(page, total);
        model.addAttribute("ships", response.getData());

        return "fleet/fleet";
    }
}
