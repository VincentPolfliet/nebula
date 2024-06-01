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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private final AtomicBoolean socketServerRunning = new AtomicBoolean(false);
    private final AtomicBoolean processRunning = new AtomicBoolean(false);

    private final String prefix;
    private final String scriptName;

    private final int remoteSocketPort = 9000;
    private final int vitePort;

    public ViteDevRunner(ViteProperties properties) {
        this.prefix = properties.baseDirectory() != null ? properties.baseDirectory() : null;
        this.scriptName = Objects.requireNonNullElse(properties.scriptName(), "dev");
        this.vitePort = Objects.requireNonNullElse(properties.devPort(), 5173);
        this.viteRemoteSocketServer = new ViteRemoteSocketServer();
        this.processRunner = new ViteProcessRunner(io);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        io.execute(() -> {
            try {
                socketServerRunning.set(true);
                viteRemoteSocketServer.start(remoteSocketPort);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                socketServerRunning.set(false);
            }
        });

        io.execute(() -> {
            try {
                processRunning.set(true);
                logger.info("Vite has started on {}", vitePort);

                ProcessBuilder builder = new ProcessBuilder(buildCommand());
                Map<String, String> env = builder.environment();
                env.put("VITE_PORT", String.valueOf(vitePort));
                env.put("VITE_SOCKET_RELOAD_PORT", String.valueOf(remoteSocketPort));

                processRunner.start(builder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                processRunning.set(false);
            }
        });
    }

    private List<String> buildCommand() {
        // params for command should be seperated entries
        List<String> commands = new ArrayList<>();

        // TODO: make this configurable
        String npmPath = "target\\node\\npm.cmd";
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
