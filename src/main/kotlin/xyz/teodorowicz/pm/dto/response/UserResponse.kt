package xyz.teodorowicz.pm.dto.response

import xyz.teodorowicz.pm.entity.*

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: String,
    val tasks: List<Task>,
    val files: List<File>,
    val messages: List<Message>,
    val notifications: List<Notification>,
    val teams: List<Team>,
)
