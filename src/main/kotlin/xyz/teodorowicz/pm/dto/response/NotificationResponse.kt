package xyz.teodorowicz.pm.dto.response

import java.time.LocalDateTime

data class NotificationResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val read: Boolean,
    val teamName: String?
)
