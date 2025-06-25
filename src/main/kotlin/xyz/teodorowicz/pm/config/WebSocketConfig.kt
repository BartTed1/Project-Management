package xyz.teodorowicz.pm.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer
import xyz.teodorowicz.pm.websocket.NotificationWebSocketHandler

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val notificationWebSocketHandler: NotificationWebSocketHandler
) : WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    // Konfiguracja klasycznego WebSocket
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(notificationWebSocketHandler, "/ws/notifications")
            .setAllowedOrigins("*") // Uwaga: poprawić na produkcji
    }

    // Konfiguracja STOMP/WebSocket Message Broker
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*")          // czysty WebSocket
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS() // SockJS fallback
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/app")
    }
}
