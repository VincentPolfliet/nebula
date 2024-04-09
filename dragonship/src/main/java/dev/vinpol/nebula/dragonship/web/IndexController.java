package dev.vinpol.nebula.dragonship.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String renderIndex(Model model) {
        Page page = new Page();
        page.setTitle("Home");

        model.addAttribute("page", page);

        return "index";
    }
}
