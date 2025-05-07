package dev.vinpol.nebula.dragonship.web.assets;


import com.fasterxml.jackson.databind.JsonNode;
import dev.vinpol.nebula.dragonship.shared.i18n.hJsonMessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class AssetController {

    private final hJsonMessageSource messageSource;

    public AssetController(hJsonMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/assets/i18n/{lang}")
    public JsonNode getAsset(@PathVariable("lang") String lang) {
        return messageSource.getMessages(Locale.forLanguageTag(lang));
    }
}
