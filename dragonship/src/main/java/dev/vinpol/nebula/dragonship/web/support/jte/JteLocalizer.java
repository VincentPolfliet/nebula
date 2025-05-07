package dev.vinpol.nebula.dragonship.web.support.jte;

import gg.jte.Content;
import gg.jte.support.LocalizationSupport;
import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;

@Component
public class JteLocalizer implements LocalizationSupport {
    private static JteLocalizer LOCALIZER;
    private final MessageSource messageSource;

    public JteLocalizer(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @PostConstruct
    void init() {
        LOCALIZER = this;
    }

    @Override
    public String lookup(String key) {
        return messageSource.getMessage(new DefaultMessageSourceResolvable(key), LocaleContextHolder.getLocale());
    }

    public static Content i18n(String key) {
        return LOCALIZER.localize(key);
    }

    public static Content i18n(String key, Object... params) {
        return LOCALIZER.localize(key, params);
    }
}
