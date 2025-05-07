package dev.vinpol.nebula.dragonship.web.support.vite;

import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ManifestLoader;
import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ManifestLoaderImpl;
import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ManifestParser;
import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ViteEnv;
//import dev.vinpol.vite.ViteProperties;
//import dev.vinpol.vite.ViteRunner;
//import dev.vinpol.vite.spring.ViteRunnerComponent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.nio.file.Path;

@Configuration
@EnableConfigurationProperties({ViteSupportProperties.class})
public class ViteSupportConfig {

    @Bean
    public ManifestLoader manifestLoader(ViteEnv viteEnv, ManifestParser manifestParser) {
        return new ManifestLoaderImpl(viteEnv, manifestParser, null);
    }

    //@Bean
    //@Profile("dev")
//    public ViteRunnerComponent viteRunnerComponent(ViteRunner runner, ViteSupportProperties properties) {
//        return new ViteRunnerComponent(runner,
//            ViteProperties.builder()
//                .workingDirectory(properties.baseDirectory() != null ? Path.of(properties.baseDirectory()) : null)
//                .runViteCommand(properties.scriptName())
//                .port(properties.devPort())
//                .build()
//        );
//    }
}
