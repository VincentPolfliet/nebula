package dev.vinpol.nebula.dragonship.web.support.vite.api.remote;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public final class ViteRemoteSocketServer implements AutoCloseable {

    private final Logger logger = LoggerFactory.getLogger(ViteRemoteSocketServer.class);

    private ServerSocket serverSocket;

    private Socket clientSocket;

    private BufferedSink output;
    private BufferedSource input;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        clientSocket = serverSocket.accept();
        output = Okio.buffer(Okio.sink(clientSocket.getOutputStream()));
        input = Okio.buffer(Okio.source(clientSocket.getInputStream()));
    }

    @Override
    public void close() throws IOException {
        if (input != null) {
            input.close();
        }

        if (output != null) {
            output.close();
        }

        if (clientSocket != null) {
            clientSocket.close();
        }

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public void sendEnd() throws IOException {
        if (clientSocket != null) {
            clientSocket.close();
        }
    }
}
