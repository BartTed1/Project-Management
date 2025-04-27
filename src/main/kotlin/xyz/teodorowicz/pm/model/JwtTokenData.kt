package xyz.teodorowicz.pm.model

data class JwtTokenData(
    val claims: Map<String, Any>,
    val token: String
)
