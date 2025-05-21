package xyz.teodorowicz.pm.enumeration

enum class SortDirection {
    ASC,
    DESC;

    companion object {
        fun fromString(value: String): SortDirection {
            return when (value.uppercase()) {
                "ASC" -> ASC
                "DESC" -> DESC
                else -> throw IllegalArgumentException("Invalid sort direction: $value")
            }
        }
    }
}