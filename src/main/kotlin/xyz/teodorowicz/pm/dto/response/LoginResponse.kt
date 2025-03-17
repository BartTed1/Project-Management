package xyz.teodorowicz.pm.dto.response

import xyz.teodorowicz.pm.entity.User

data class LoginResponse(
    val token: String,
    val user: User
)
