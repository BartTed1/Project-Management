package xyz.teodorowicz.pm.enumeration.project

enum class ProjectPriority {
    LOW,
    NORMAL,
    MEDIUM,
    HIGH;

    companion object {
        fun fromString(value: String): ProjectPriority {
            return when (value.uppercase()) {
                "LOW" -> LOW
                "NORMAL" -> NORMAL
                "MEDIUM" -> MEDIUM
                "HIGH" -> HIGH
                else -> throw IllegalArgumentException("Unknown priority: $value")
            }
        }
    }
}