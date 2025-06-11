package xyz.teodorowicz.pm.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import xyz.teodorowicz.pm.dto.response.NotificationResponse
import java.util.concurrent.ConcurrentHashMap

@Component
class WebSocketSessionManager(
    private val objectMapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(WebSocketSessionManager::class.java)
    private val userSessions = ConcurrentHashMap<Long, MutableSet<WebSocketSession>>()

    fun addSession(userId: Long, session: WebSocketSession) {
        userSessions.computeIfAbsent(userId) { ConcurrentHashMap.newKeySet() }.add(session)
        logger.info("Added WebSocket session for user: $userId")
    }

    fun removeSession(userId: Long, session: WebSocketSession) {
        userSessions[userId]?.let { sessions ->
            sessions.remove(session)
            if (sessions.isEmpty()) {
                userSessions.remove(userId)
            }
        }
        logger.info("Removed WebSocket session for user: $userId")
    }

    fun sendNotificationToUser(userId: Long, notification: NotificationResponse) {
        userSessions[userId]?.let { sessions ->
            val message = objectMapper.writeValueAsString(notification)
            val textMessage = TextMessage(message)
            
            sessions.removeIf { session ->
                try {
                    if (session.isOpen) {
                        session.sendMessage(textMessage)
                        false
                    } else {
                        true
                    }
                } catch (e: Exception) {
                    logger.error("Error sending notification to user $userId", e)
                    true
                }
            }
            
            if (sessions.isEmpty()) {
                userSessions.remove(userId)
            }
        }
    }

    fun isUserConnected(userId: Long): Boolean {
        return userSessions[userId]?.any { it.isOpen } ?: false
    }

    fun getActiveSessionsCount(): Int {
        return userSessions.values.sumOf { sessions ->
            sessions.count { it.isOpen }
        }
    }
}
