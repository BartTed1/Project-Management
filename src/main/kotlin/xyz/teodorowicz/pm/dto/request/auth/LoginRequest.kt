package xyz.teodorowicz.pm.dto.request.auth

data class LoginRequest(
    val email: String,
    val password: String
)
