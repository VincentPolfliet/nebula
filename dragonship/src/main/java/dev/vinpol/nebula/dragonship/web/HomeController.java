package dev.vinpol.nebula.dragonship.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/", ""})
    public String renderIndex(Model model) {
        return "redirect:/fleet";
    }
}
