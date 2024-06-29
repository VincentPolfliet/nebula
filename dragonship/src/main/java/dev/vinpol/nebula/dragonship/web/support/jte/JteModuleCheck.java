package dev.vinpol.nebula.dragonship.web.support.jte;

import gg.jte.springframework.boot.autoconfigure.JteProperties;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

// Jte will only work in IdeaJ as submodule
// if the current working directory has the expected template location
@Component
@ConditionalOnProperty(value = "gg.jte.developmentMode", havingValue = "true")
public class JteModuleCheck {

    private final Logger logger = LoggerFactory.getLogger(JteModuleCheck.class);

    private final JteProperties properties;

    public JteModuleCheck(JteProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        // this is how you get the current directory
        Path currentPath = Path.of("").toAbsolutePath();

        String templateLocation = properties.getTemplateLocation();
        Path jteTemplateLocation = currentPath.resolve(templateLocation);

        if (!Files.exists(jteTemplateLocation)) {
            logger.error("JteTemplateLocation '{}' does not exist, has it been set as the working directory in IntelliJ?", jteTemplateLocation);
            throw new RuntimeException("JteTemplateLocation '%s' does not exist".formatted(jteTemplateLocation));
        }
    }
}
