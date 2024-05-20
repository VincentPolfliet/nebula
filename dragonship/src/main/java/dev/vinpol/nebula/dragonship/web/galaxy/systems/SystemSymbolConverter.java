package dev.vinpol.nebula.dragonship.web.galaxy.systems;


import dev.vinpol.nebula.dragonship.sdk.SystemSymbol;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SystemSymbolConverter implements Converter<String, SystemSymbol> {
    @Override
    public SystemSymbol convert(@NotNull String source) {
        return SystemSymbol.tryParse(source);
    }
}
