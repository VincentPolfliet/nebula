package dev.vinpol.nebula.dragonship.shared.i18n;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hjson.JsonValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;

import static dev.vinpol.nebula.dragonship.utils.StringUtils.appendIfNotStartsWith;

@Component(value = "messageSource")
public class hJsonMessageSource extends AbstractMessageSource {

    private final Logger logger = LoggerFactory.getLogger(hJsonMessageSource.class);

    private static final String DEFAULT_JSON_MESSAGES_PATH = "i18n/messages";

    private final ObjectMapper objectMapper;
    private final MessagesCache messagesCache;

    private final String baseName;
    private final String jsonMessagePath;

    public hJsonMessageSource(@Value("${i18n-json.path:}") String jsonMessagePath,
                              MessageSourceProperties properties,
                              ObjectMapper objectMapper,
                              MessagesCache messagesCache) {
        this.jsonMessagePath = (jsonMessagePath == null || jsonMessagePath.isBlank())
            ? DEFAULT_JSON_MESSAGES_PATH
            : jsonMessagePath;

        logger.debug("HJSON message path: {}", this.jsonMessagePath);

        this.baseName = properties.getBasename().getFirst();
        this.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
        this.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());

        this.objectMapper = objectMapper;
        this.messagesCache = messagesCache;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        if (!messagesCache.hasItem(locale)) {
            loadMessages(locale);
        }

        JsonNode nodeForLocale = messagesCache.get(locale);
        JsonNode nodeAtCode = nodeForLocale.at(appendIfNotStartsWith('/', code).replace('.', '/'));

        String text = nodeAtCode.asText("__" + code + "__");
        return new MessageFormat(text, locale);
    }

    private void loadMessages(Locale locale) {
        String filePath = jsonMessagePath + "/" + baseName + "_" + locale.getLanguage().toLowerCase() + ".hjson";
        ClassPathResource resource = new ClassPathResource(filePath);
        logger.debug("Loading HJSON file: {}", resource);

        try (var stream = resource.getInputStream();
             var output = new ByteArrayOutputStream()) {
            stream.transferTo(output);
            String hjsonContent = output.toString(StandardCharsets.UTF_8);

            String json = JsonValue.readHjson(hjsonContent).toString();
            JsonNode jsonNode = objectMapper.readTree(json);
            messagesCache.set(locale, jsonNode);

        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load HJSON file: " + filePath, e);
        }
    }

    public JsonNode getMessages(Locale locale) {
        if (!messagesCache.hasItem(locale)) {
            loadMessages(locale);
        }

        return messagesCache.get(locale);
    }
}
