package xyz.teodorowicz.pm.dto.chat

data class ChatMessage(
    val userId: Int,
    val teamId: Int,
    val content: String,
    val timestamp: String? = null
)
