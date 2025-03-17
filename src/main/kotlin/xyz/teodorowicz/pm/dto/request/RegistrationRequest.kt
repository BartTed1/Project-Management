package xyz.teodorowicz.pm.dto.request

data class RegistrationRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val login: String,
)
