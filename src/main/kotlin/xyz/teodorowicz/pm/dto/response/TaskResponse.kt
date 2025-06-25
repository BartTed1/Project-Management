package xyz.teodorowicz.pm.dto.response

import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.entity.User
import java.time.LocalDateTime

data class TaskResponse (
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val deadline: LocalDateTime,
    val endAt: LocalDateTime,
    val status: String,
    val priority: String,
    val reminder: String,
    val user: User,
    val team : Team

    )