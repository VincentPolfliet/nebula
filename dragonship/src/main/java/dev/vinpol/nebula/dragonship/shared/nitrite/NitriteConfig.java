package dev.vinpol.nebula.dragonship.shared.nitrite;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.dizitart.no2.common.module.NitriteModule;
import org.dizitart.no2.migration.Migration;
import org.dizitart.no2.mvstore.MVStoreModule;
import org.dizitart.no2.store.memory.InMemoryStoreModule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.nio.file.Path;
import java.util.List;

@Configuration
public class NitriteConfig {

    @Bean
    public Nitrite nitrite(List<NitriteModule> modules, List<Migration> migrations) {
        var builder = Nitrite.builder();

        if (!modules.isEmpty()) {
            for (NitriteModule module : modules) {
                builder.loadModule(module);
            }
        } else {
            builder.loadModule(new InMemoryStoreModule());
        }

        for (Migration migration : migrations) {
            builder.addMigrations(migration);
        }

        return builder.openOrCreate();
    }

    @Bean
    public JacksonMapperModule jacksonModule() {
        Module[] modules = ObjectMapper.findModules().toArray(new Module[0]);
        return new JacksonMapperModule(modules);
    }
}
