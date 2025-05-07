package dev.vinpol.nebula.dragonship.web.settings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.sdk.NebulaProperties;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class SettingsController {

    private final NebulaProperties nebulaProperties;
    private final ObjectMapper objectMapper;

    public SettingsController(NebulaProperties nebulaProperties, ObjectMapper objectMapper) {
        this.nebulaProperties = nebulaProperties;
        this.objectMapper = objectMapper;
    }

    @GetMapping(value = "/settings")
    public String settings(Model model) throws JsonProcessingException {
        model.addAttribute("dataJson", objectMapper.writeValueAsString(Map.of("token", nebulaProperties.token())));

        return "settings/settings";
    }
}
