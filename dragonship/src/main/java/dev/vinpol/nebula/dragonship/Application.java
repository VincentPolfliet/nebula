package dev.vinpol.nebula.dragonship;

import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.ViteEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(Application.class);

    private final ViteEnv viteEnv;

    private int port;

    public Application(ViteEnv viteEnv) {
        this.viteEnv = viteEnv;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Local: http://localhost:{}/", port);

        if (viteEnv.isDev()) {
            logger.info("Vite: {}", viteEnv.getBaseUrl());
        }
    }

    @EventListener
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
        this.port = event.getWebServer().getPort();
    }

}
