package xyz.teodorowicz.pm.enumeration.project

enum class ProjectStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    ON_HOLD,
    CANCELLED;

    companion object {
        fun fromString(value: String): ProjectStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unknown project status: $value")
        }
    }
}