package dev.vinpol.nebula.dragonship.web.settings;

import dev.vinpol.nebula.dragonship.sdk.NebulaProperties;
import dev.vinpol.nebula.dragonship.shared.kv.KVStore;
import dev.vinpol.nebula.dragonship.web.HtmlPage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    private final NebulaProperties nebulaProperties;

    public SettingsController(NebulaProperties nebulaProperties) {
        this.nebulaProperties = nebulaProperties;
    }

    @GetMapping(value = "/settings")
    public String settings(Model model) {
        HtmlPage page = new HtmlPage();
        page.setTitle("Settings");

        model.addAttribute("page", page);
        model.addAttribute("token", nebulaProperties.token());

        return "settings/settings";
    }
}
