package dev.vinpol.nebula.dragonship.web.config.support.vite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ViteEnv {
    private static SpringViteEnv env;

    ViteEnv() {
    }

    @Autowired
    void setEnv(SpringViteEnv env) {
        ViteEnv.env = env;
    }

    public static boolean isDev() {
        return env.isDev();
    }
}
