package dev.vinpol.nebula.dragonship.web.galaxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.web.Page;
import dev.vinpol.spacetraders.sdk.api.AgentsApi;
import dev.vinpol.spacetraders.sdk.api.SystemsApi;
import dev.vinpol.spacetraders.sdk.models.GetSystem200Response;
import dev.vinpol.spacetraders.sdk.models.GetSystems200Response;
import dev.vinpol.spacetraders.sdk.models.System;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class GalaxyController {

    private final AgentsApi agentsApi;
    private final SystemsApi systemsApi;
    private final ObjectMapper objectMapper;

    public GalaxyController(AgentsApi agentsApi, SystemsApi systemsApi, ObjectMapper objectMapper) {
        this.agentsApi = agentsApi;
        this.systemsApi = systemsApi;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/galaxy")
    public String galaxy() {
        return "redirect:/galaxy/systems";
    }

    @GetMapping("/galaxy/systems")
    public String getGalaxySystems(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "total", defaultValue = "10") int total,
                                   Model model) {
        Page content = new Page();
        content.setTitle("Galaxy");

        GetSystems200Response systems = systemsApi.getSystems(page, total);
        model.addAttribute("page", content);

        model.addAttribute("currentPage", page);
        model.addAttribute("total", total);
        model.addAttribute("systems", systems.getData());

        return "galaxy/index";
    }

    @GetMapping("/galaxy/system/{system}")
    public String galaxy(@PathVariable("system") String symbol, Model model) {
        Page content = new Page();
        content.setTitle("System");

        model.addAttribute("page", content);

        GetSystem200Response response = systemsApi.getSystem(symbol);
        System system = response.getData();
        model.addAttribute("system", system);


        return "galaxy/system";
    }

    @GetMapping("/galaxy/system/{system}/map")
    public String map(@PathVariable("system") String symbol, Model model) throws JsonProcessingException {
        Objects.requireNonNull(symbol);

        GetSystem200Response response = systemsApi.getSystem(symbol);
        System system = response.getData();

        String mapDataJson = objectMapper.writerWithDefaultPrettyPrinter()
            .writeValueAsString(MapData.ofSystem(system));

        model.addAttribute("map", mapDataJson);

        return "galaxy/map";
    }
}
