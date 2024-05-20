package dev.vinpol.nebula.dragonship.web.galaxy.systems;

import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import dev.vinpol.nebula.dragonship.web.Page;
import dev.vinpol.nebula.dragonship.web.utils.PagingUtils;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.GetSystemWaypoints200Response;
import dev.vinpol.spacetraders.sdk.models.System;
import dev.vinpol.spacetraders.sdk.models.WaypointType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/galaxy/system")
public class SystemController {

    private final SystemsApi systemsApi;

    public SystemController(SystemsApi systemsApi) {
        this.systemsApi = systemsApi;
    }

    @GetMapping(value = {"/{systemSymbol}", "/{systemSymbol}/waypoints"})
    public String getSystemPage(@PathVariable("systemSymbol") SystemSymbol symbol,
                                @RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "total", defaultValue = "10") int total,
                                @RequestParam(value = "type", required = false) WaypointType type,
                                Model model) {
        Page content = new Page();
        content.setTitle("System");

        model.addAttribute("page", content);

        String systemSymbol = symbol.system();
        System system = systemsApi.getSystem(systemSymbol).getData();
        GetSystemWaypoints200Response response = systemsApi.getSystemWaypoints(systemSymbol, page, total, type);

        model.addAttribute("system", system);

        PagingUtils.setMetaOnModel(model, response.getMeta());
        model.addAttribute("waypoints", response.getData());

        return "galaxy/systems/system";
    }
}
