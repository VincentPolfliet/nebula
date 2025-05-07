package dev.vinpol.nebula.dragonship.web.support.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;

@Component
public class LocaleHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) {
        String acceptLang = request.getHeader("Accept-Language");
        String lang = null;

        if (acceptLang != null && !acceptLang.isBlank()) {
            // take the first range, e.g. "fr-CH,fr;q=0.9,en;q=0.8"
            Locale preferred = Locale.lookup(
                Locale.LanguageRange.parse(acceptLang),
                List.of(Locale.getAvailableLocales())
            );

            if (preferred != null) {
                lang = preferred.getLanguage();
            }
        }

        if (lang == null) {
            lang = LocaleContextHolder.getLocale().getLanguage();
        }

        response.setHeader("Content-Language", lang);

        return true;
    }
}
