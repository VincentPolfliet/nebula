package dev.vinpol.nebula.dragonship.web.support.jte;

import gg.jte.CodeResolver;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.DirectoryCodeResolver;
import gg.jte.springframework.boot.autoconfigure.JteConfigurationException;
import gg.jte.springframework.boot.autoconfigure.JteProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

@Configuration
public class CustomJteConfig {

    @Bean
    public TemplateEngine jteTemplateEngine(JteProperties properties, @Value("${gg.jte.classLocation:jte-classes}") String pathClassLocation) {
        if (properties.isDevelopmentMode() && properties.usePreCompiledTemplates()) {
            throw new JteConfigurationException("You can't use development mode and precompiledTemplates together");
        }
        if (properties.usePreCompiledTemplates()) {
            // Templates will need to be compiled by the maven/gradle build task
            return TemplateEngine.createPrecompiled(ContentType.Html);
        }

        if (properties.isDevelopmentMode()) {
            // Here, a jte file watcher will recompile the jte templates upon file save (the web browser will auto-refresh)
            // If using IntelliJ, use Ctrl-F9 to trigger an auto-refresh when editing non-jte files.
            String[] split = properties.getTemplateLocation().split("/");
            CodeResolver codeResolver = new DirectoryCodeResolver(FileSystems.getDefault().getPath("", split));
            return TemplateEngine.create(codeResolver, Paths.get(pathClassLocation), ContentType.Html, getClass().getClassLoader());
        }

        throw new JteConfigurationException("You need to either set gg.jte.usePrecompiledTemplates or gg.jte.developmentMode to true ");
    }
}
