package dev.vinpol.nebula.dragonship.web.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.vinpol.nebula.dragonship.automation.events.out.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ShipRowUpdateHandler extends TextWebSocketHandler implements ShipEventListener {

    private final ObjectMapper objectMapper;
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public ShipRowUpdateHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
        this.sessions.remove(session);
    }

    @EventListener
    public void onCargoIsFull(CargoIsFullEvent event) throws IOException {
        publish(event.shipSymbol());
    }

    @Override
    @EventListener
    public void onFuelIsAlmostEmpty(FuelIsAlmostEmptyEvent event) throws IOException {
        publish(event.shipSymbol());
    }

    @Override
    @EventListener
    public void onWaitUntilArrival(NavigatingToEvent event) throws IOException {
        publish(event.shipSymbol());
    }

    @Override
    @EventListener
    public void onArrivedAt(ArrivatedAtDestinationEvent event) throws IOException {
        publish(event.shipSymbol());
    }

    @Override
    @EventListener
    public void onWaitCooldown(WaitUntilCooldownEvent event) throws IOException {
        publish(event.shipSymbol());
    }

    private void publish(String shipSymbol) throws IOException {
        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(objectMapper.writeValueAsString(Map.of("ship", shipSymbol))));
        }
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
        this.sessions.remove(session);
    }
}
