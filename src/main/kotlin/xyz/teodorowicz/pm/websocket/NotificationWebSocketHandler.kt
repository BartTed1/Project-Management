package xyz.teodorowicz.pm.websocket

import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.CopyOnWriteArrayList

@Component
class NotificationWebSocketHandler : TextWebSocketHandler() {

    // Lista aktywnych sesji
    private val sessions: MutableList<WebSocketSession> = CopyOnWriteArrayList()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        sessions.add(session)
        println("🔌 Połączono: ${session.id}")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        println("❌ Rozłączono: ${session.id}")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("📨 Otrzymano wiadomość: ${message.payload} od ${session.id}")

        // Przykład: broadcast do wszystkich
        for (s in sessions) {
            if (s.isOpen) {
                s.sendMessage(TextMessage("Notyfikacja: ${message.payload}"))
            }
        }
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("⚠️ Błąd transportu: ${exception.message}")
    }
}
