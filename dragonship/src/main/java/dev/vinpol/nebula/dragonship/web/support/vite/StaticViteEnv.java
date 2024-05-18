package dev.vinpol.nebula.dragonship.web.support.vite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class StaticViteEnv {
    private static SpringViteEnv env;

    StaticViteEnv() {
    }

    @Autowired
    void setEnv(SpringViteEnv env) {
        StaticViteEnv.env = env;
    }

    public static boolean isDev() {
        return env.isDev();
    }
}
