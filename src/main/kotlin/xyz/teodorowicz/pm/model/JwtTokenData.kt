package xyz.teodorowicz.pm.model

import xyz.teodorowicz.pm.enumeration.user.SystemRole

data class JwtTokenClaims(
    val userId: Long,
    val email: String,
    val role: SystemRole,
) {
    companion object {
        fun fromMap(claims: Map<String, Any>): JwtTokenClaims {
            return JwtTokenClaims(
                userId = (claims["userId"] as Number).toLong(),
                email = claims["email"] as String,
                role = SystemRole.fromString(claims["role"] as String)
            )
        }
    }
}

data class JwtTokenData(
    val claims: JwtTokenClaims, //Map<String, Any>,
    val token: String
)
