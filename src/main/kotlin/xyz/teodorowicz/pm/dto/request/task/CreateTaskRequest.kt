package xyz.teodorowicz.pm.dto.request.task

import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.entity.User
import java.time.LocalDateTime

data class CreateTaskRequest (
    val title: String,
    val description: String,
    val deadline: LocalDateTime,
    val priority: String,

    val userId: Long,
    val teamId: Long
)