package xyz.teodorowicz.pm.dto.request

data class UpdateUserRequest(
    val name: String?,
    val email: String?,
    val role: String?,
)
