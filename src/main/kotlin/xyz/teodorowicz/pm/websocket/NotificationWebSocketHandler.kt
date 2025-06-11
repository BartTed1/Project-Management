package xyz.teodorowicz.pm.websocket

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import xyz.teodorowicz.pm.service.SecurityService

@Component
class NotificationWebSocketHandler(
    private val webSocketSessionManager: WebSocketSessionManager,
    private val securityService: SecurityService
) : TextWebSocketHandler() {
    
    private val logger = LoggerFactory.getLogger(NotificationWebSocketHandler::class.java)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        try {
            val userId = authenticateSession(session)
            if (userId != null) {
                session.attributes["userId"] = userId
                webSocketSessionManager.addSession(userId, session)
                logger.info("WebSocket connection established for user: $userId")
            } else {
                logger.warn("Authentication failed for WebSocket connection")
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Authentication failed"))
            }
        } catch (e: Exception) {
            logger.error("Error establishing WebSocket connection", e)
            session.close(CloseStatus.SERVER_ERROR.withReason("Server error"))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = session.attributes["userId"] as? Long
        if (userId != null) {
            webSocketSessionManager.removeSession(userId, session)
            logger.info("WebSocket connection closed for user: $userId")
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // Handle ping/pong or other messages if needed
        logger.debug("Received message: ${message.payload}")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        logger.error("WebSocket transport error", exception)
        val userId = session.attributes["userId"] as? Long
        if (userId != null) {
            webSocketSessionManager.removeSession(userId, session)
        }
    }

    private fun authenticateSession(session: WebSocketSession): Long? {
        return try {
            // Extract token from query parameters or headers
            val token = extractTokenFromSession(session)
            if (token != null) {
                val tokenData = securityService.parseJwtToken(token)
                tokenData.claims.userId
            } else {
                null
            }
        } catch (e: Exception) {
            logger.error("Error authenticating WebSocket session", e)
            null
        }
    }

    private fun extractTokenFromSession(session: WebSocketSession): String? {
        // Try to get token from query parameters
        val query = session.uri?.query
        if (query != null) {
            val params = query.split("&").associate {
                val parts = it.split("=", limit = 2)
                if (parts.size == 2) {
                    parts[0] to parts[1]
                } else {
                    parts[0] to ""
                }
            }
            val token = params["token"]
            if (token != null && token.isNotEmpty()) {
                return token
            }
        }

        // Try to get token from headers
        val authHeader = session.handshakeHeaders["Authorization"]?.firstOrNull()
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7)
        }

        return null
    }
}
