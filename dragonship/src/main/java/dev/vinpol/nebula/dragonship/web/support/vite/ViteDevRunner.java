package dev.vinpol.nebula.dragonship.web.support.vite;

import dev.vinpol.nebula.dragonship.web.support.vite.api.remote.ViteRemoteSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Profile("dev")
public class ViteDevRunner implements ApplicationRunner, AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(ViteDevRunner.class);

    private final ExecutorService io = Executors.newCachedThreadPool(
        Thread.ofVirtual()
            .name("vite-dev-io-", 1)
            .factory()
    );

    private final ViteRemoteSocketServer viteRemoteSocketServer;
    private final ViteProcessRunner processRunner;

    private final String prefix;
    private final String scriptName;

    private final int port = 9000;

    public ViteDevRunner(ViteProperties properties) {
        this.prefix = properties.baseDirectory() != null ? properties.baseDirectory() : null;
        this.scriptName = Objects.requireNonNullElse(properties.scriptName(), "dev");

        this.viteRemoteSocketServer = new ViteRemoteSocketServer();
        this.processRunner = new ViteProcessRunner(io);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // TODO: only start process when server is already running
        io.execute(() -> {
            try {
                viteRemoteSocketServer.start(port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        io.execute(() -> {
            try {
                processRunner.start(new ProcessBuilder(buildCommand()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<String> buildCommand() {
        // params for command should be seperated entries
        List<String> commands = new ArrayList<>();

        // TODO: set npm location to either find it on current path or use the maven frontend plugin ???
        String npmPath = "C:\\Program Files\\nodejs\\npm.cmd";

        commands.add(npmPath);

        if (prefix != null) {
            commands.add("--prefix");
            commands.add(prefix);
        }

        commands.add("run");
        commands.add(scriptName);

        return commands;
    }

    @Override
    public void close() throws Exception {
        logger.trace("close called");

        logger.trace("closing socket server");
        viteRemoteSocketServer.close();

        logger.trace("closing process");
        processRunner.close();

        logger.trace("shutting down IO scheduler");
        io.shutdown();
    }
}
