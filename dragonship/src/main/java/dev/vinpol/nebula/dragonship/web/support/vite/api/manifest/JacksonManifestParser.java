package dev.vinpol.nebula.dragonship.web.support.vite.api.manifest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JacksonManifestParser implements ManifestParser {

    private final ObjectMapper objectMapper;

    public JacksonManifestParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Manifest parse(Reader reader) {
        try {
            JsonNode jsonNode = objectMapper.reader()
                    .readTree(reader);

            return new JsonNodeManifest(jsonNode, objectMapper);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final static class JsonNodeManifest implements Manifest {

        private final JsonNode node;
        private final ObjectMapper objectMapper;

        private final Map<String, Chunk> chunkCache = new LinkedHashMap<>();

        private JsonNodeManifest(JsonNode node, ObjectMapper objectMapper) {
            this.node = node;
            this.objectMapper = objectMapper;
        }

        @Override
        public Chunk getChunk(String name) {
            if (chunkCache.containsKey(name)) {
                return chunkCache.get(name);
            }

            try {
                var result = objectMapper.treeToValue(node.get(name), Chunk.class);
                chunkCache.put(name, result);
                return result;
            } catch (JsonProcessingException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
