package xyz.teodorowicz.pm.controller

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import xyz.teodorowicz.pm.dto.chat.ChatMessage
import java.time.LocalDateTime

@Controller
class ChatControllerImp : ChatController {
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    override fun sendMessage(message: ChatMessage): ChatMessage {
        println("BACKEND LOG: Otrzymano wiadomość: $message")
        return message.copy(timestamp = LocalDateTime.now().toString())
    }
}

