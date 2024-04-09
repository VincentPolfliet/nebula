package dev.vinpol.nebula.dragonship.web.vite.manifest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.Chunk;
import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.JacksonManifestParser;
import dev.vinpol.nebula.dragonship.web.support.vite.api.manifest.Manifest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class JacksonManifestParserTest {

    JacksonManifestParser sut;

    @BeforeEach
    void setup() {
        ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

        sut = new JacksonManifestParser(objectMapper);
    }

    @Test
    void parse() {
        String json = """
            {
              "main.js": {
                "file": "assets/main.4889e940.js",
                "src": "main.js",
                "isEntry": true,
                "dynamicImports": ["views/foo.js"],
                "css": ["assets/main.b82dbe22.css"],
                "assets": ["assets/asset.0ab0f9cd.png"],
                "imports": ["_shared.83069a53.js"]
              },
              "views/foo.js": {
                "file": "assets/foo.869aea0d.js",
                "src": "views/foo.js",
                "isDynamicEntry": true,
                "imports": ["_shared.83069a53.js"]
              },
              "_shared.83069a53.js": {
                "file": "assets/shared.83069a53.js",
                "css": ["assets/shared.a834bfc3.css"]
              }
            }
            """;

        Manifest manifest = sut.parse(new StringReader(json));

        assertThat(manifest).isNotNull();

        Chunk mainJsRecord = manifest.getChunk("main.js");
        assertThat(mainJsRecord.getFile()).isEqualTo("assets/main.4889e940.js");
        assertThat(mainJsRecord.getSrc()).isEqualTo("main.js");
        assertThat(mainJsRecord.isEntry()).isTrue();
        assertThat(mainJsRecord.getCss()).containsOnly("assets/main.b82dbe22.css");
        assertThat(mainJsRecord.getDynamicImports()).containsOnly("views/foo.js");
        assertThat(mainJsRecord.getAssets()).containsOnly("assets/asset.0ab0f9cd.png");
        assertThat(mainJsRecord.getImports()).containsOnly("_shared.83069a53.js");

        Chunk fooJsViewRecord = manifest.getChunk("views/foo.js");
        assertThat(fooJsViewRecord.getFile()).isEqualTo("assets/foo.869aea0d.js");
        assertThat(fooJsViewRecord.getSrc()).isEqualTo("views/foo.js");
        assertThat(fooJsViewRecord.getImports()).containsOnly("_shared.83069a53.js");
        assertThat(fooJsViewRecord.isDynamicEntry()).isTrue();

        Chunk sharedRecord = manifest.getChunk("_shared.83069a53.js");
        assertThat(sharedRecord.getFile()).isEqualTo( "assets/shared.83069a53.js");
        assertThat(sharedRecord.getCss()).containsOnly( "assets/shared.a834bfc3.css");
    }
}
