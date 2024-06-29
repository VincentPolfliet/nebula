package dev.vinpol.nebula.dragonship.web.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final BeanFactory beanFactory;

    public WebSocketConfig(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        var handler = new PerConnectionWebSocketHandler(ShipRowUpdateHandler.class);
        handler.setBeanFactory(beanFactory);
        registry.addHandler(handler, "/ws/ship").setAllowedOrigins("*");
    }
}
