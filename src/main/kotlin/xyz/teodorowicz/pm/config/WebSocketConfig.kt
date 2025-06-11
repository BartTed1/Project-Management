package xyz.teodorowicz.pm.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import xyz.teodorowicz.pm.websocket.NotificationWebSocketHandler

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val notificationWebSocketHandler: NotificationWebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(notificationWebSocketHandler, "/ws/notifications")
            .setAllowedOrigins("*") // Configure this properly for production
    }
}
