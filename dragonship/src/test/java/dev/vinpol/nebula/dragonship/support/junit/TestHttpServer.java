package dev.vinpol.nebula.dragonship.support.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.UncheckedIOException;

public class TestHttpServer implements AutoCloseable {

    private final MockWebServer server = new MockWebServer();
    private final ObjectMapper objectMapper;

    public TestHttpServer() {
        objectMapper = new ObjectMapper()
            .findAndRegisterModules();
    }

    public void enqueue(Object object) {
        enqueue(object, 200);
    }

    public void enqueue(Object object, int responseCode) {
        try {
            String json = objectMapper.writeValueAsString(object);

            server.enqueue(
                new MockResponse()
                    .setResponseCode(responseCode)
                    .setHeader("content-type", "application/json; charset=utf-8")
                    .setBody(json)
            );
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public HttpUrl url(String path) {
        return server.url(path);
    }

    @Override
    public void close() throws Exception {
        server.close();
    }
}
