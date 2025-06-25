package xyz.teodorowicz.pm.controller

import xyz.teodorowicz.pm.dto.chat.ChatMessage

interface ChatController {
    fun sendMessage(message: ChatMessage): ChatMessage
}