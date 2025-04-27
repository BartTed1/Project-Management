package xyz.teodorowicz.pm.dto.request.project

data class AssignUserToProjectRequest(
    val userId: List<Long>,
    val role: String
)
