package dev.vinpol.nebula.dragonship.web.support.vite;

import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;

class ViteProcessRunner implements AutoCloseable {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";

    private final Logger logger = LoggerFactory.getLogger(ViteProcessRunner.class);

    private final Executor io;

    private Process process;

    public ViteProcessRunner(Executor io) {
        this.io = io;
    }

    public void start(ProcessBuilder processBuilder) throws IOException {
        this.process = processBuilder.start();

        StreamGobbler stdOutStream = new StreamGobbler(process.getInputStream(), StreamGobbler.Type.OUT);
        io.execute(stdOutStream);
        StreamGobbler stdErrStream = new StreamGobbler(process.getErrorStream(), StreamGobbler.Type.ERROR);
        io.execute(stdErrStream);
    }

    @Override
    public void close() throws Exception {
        if (process == null) {
            return;
        }

        if (process.isAlive()) {
            process.destroy();
        }

        if (process.isAlive()) {
            process.destroyForcibly();
        }
    }

    private final class StreamGobbler implements Runnable {
        private final InputStream is;
        private final Type type;
        private final Logger logger;

        private StreamGobbler(InputStream is, Type type) {
            this.is = is;
            this.type = type;
            this.logger = ViteProcessRunner.this.logger;
        }

        public void run() {
            try (var source = Okio.buffer(Okio.source(is))) {
                String line = null;

                while ((line = source.readUtf8Line()) != null) {
                    if (type == Type.ERROR) {
                        if (logger.isErrorEnabled()) {
                            logger.error(ANSI_RED + "{}" + ANSI_RESET, line);
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("{}", line);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        enum Type {
            ERROR,
            OUT;
        }
    }
}
