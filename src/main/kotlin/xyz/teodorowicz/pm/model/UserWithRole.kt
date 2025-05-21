package xyz.teodorowicz.pm.model

import xyz.teodorowicz.pm.entity.User

data class UserWithRole(
    val user: User,
    val role: String
)
