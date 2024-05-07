package dev.vinpol.nebula.dragonship.web.settings;

import dev.vinpol.nebula.dragonship.shared.kv.KVStore;
import dev.vinpol.nebula.dragonship.web.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {

    private final KVStore kvstore;

    public SettingsController(KVStore kvstore) {
        this.kvstore = kvstore;
    }

    @GetMapping(value = "/settings")
    public String settings(Model model) {
        Page page = new Page();
        page.setTitle("Settings");

        model.addAttribute("page", page);
        model.addAttribute("token", kvstore.get("token"));

        return "settings/settings";
    }
}
