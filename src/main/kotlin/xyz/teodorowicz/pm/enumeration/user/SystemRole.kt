package xyz.teodorowicz.pm.enumeration.user

enum class SystemRole {
    USER,
    ADMIN;

    companion object {
        fun fromString(role: String): SystemRole {
            return when (role) {
                "USER" -> USER
                "ADMIN" -> ADMIN
                else -> throw IllegalArgumentException("Unknown role: $role")
            }
        }
    }
}