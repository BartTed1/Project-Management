package xyz.teodorowicz.pm.mapper

import xyz.teodorowicz.pm.dto.response.TaskResponse
import xyz.teodorowicz.pm.entity.Task

fun Task.toResponse(): TaskResponse = TaskResponse(
    id = this.id.toLong(),
    title = this.title,
    description = this.description ?: "",
    createdAt = this.createdAt,
    updatedAt = this.updatedAt,
    deadline = this.deadline,
    endAt = this.endAt ?: this.deadline,
    status = this.status,
    priority = this.priority,
    reminder = this.reminder,
    user = this.user!!,
    team = this.team!!
)