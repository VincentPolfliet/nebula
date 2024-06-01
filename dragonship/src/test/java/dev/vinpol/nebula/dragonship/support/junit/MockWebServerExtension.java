package dev.vinpol.nebula.dragonship.support.junit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.extension.*;

import java.io.UncheckedIOException;

public class MockWebServerExtension implements BeforeAllCallback,
    BeforeEachCallback,
    AfterEachCallback,
    AfterAllCallback, ParameterResolver {

    private final MockWebServer server = new MockWebServer();
    private final ObjectMapper objectMapper;

    public MockWebServerExtension() {
        objectMapper = new ObjectMapper()
            .findAndRegisterModules();
    }

    public void enqueue(Object object) {
        enqueue(object, 200);
    }

    public void enqueue(Object object, int responseCode) {
        try {
            server.enqueue(
                new MockResponse()
                    .setResponseCode(responseCode)
                    .setHeader("content-type", "application/json; charset=utf-8")
                    .setBody(objectMapper.writeValueAsString(object))
            );
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        server.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        server.close();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        // Check if the parameter is of the type you want to inject
        return parameterContext.getParameter().getType() == MockWebServer.class
            || parameterContext.getParameter().getType() == MockWebServerExtension.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();

        if (parameterType == MockWebServer.class) {
            return server;
        }

        if (parameterType == MockWebServerExtension.class) {
            return this;
        }

        throw new ParameterResolutionException("parameter couldn't be resolved");
    }

    public HttpUrl url(String path) {
        return server.url(path);
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

    }
}
