package xyz.teodorowicz.pm.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import xyz.teodorowicz.pm.entity.User
import java.util.*

@Service
class SecurityServiceImpl(
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    @Value("\${jwt.secret}")
    private val jwtSecret: String,
    @Value("\${jwt.expiration}")
    private val jwtExpiration: Long
) : SecurityService() {

    override fun verifyToken(token: String): Boolean {
        try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
                .build()
                .parseClaimsJws(token)

            return !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            println("Token verification failed: ${e.message}")
            return false
        }
    }

    override fun comparePassword(password: String, hashedPassword: String): Boolean {
        return bCryptPasswordEncoder.matches(password, hashedPassword)
    }

    override fun generateToken(user: User): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpiration)

        return Jwts.builder()
            .setSubject(user.id.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .claim("userId", user.id)
            .claim("email", user.email)
            .claim("role", user.role)
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .compact()
    }

    override fun hashPassword(password: String): String {
        return bCryptPasswordEncoder.encode(password)
    }

    override fun getTokenClaims(token: String): Map<String, Any> {
        val claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .build()
            .parseClaimsJws(token)
            .body
        return claims
    }

    override fun getRoleFromToken(token: String): String {
        return try {
            getTokenClaims(token)["role"] as String
        } catch (e: Exception) {
            throw RuntimeException("Invalid token")
        }
    }

    override fun parseJwtToken(token: String): xyz.teodorowicz.pm.model.JwtTokenData {
        val claims = getTokenClaims(token)
        val jwtTokenClaims = xyz.teodorowicz.pm.model.JwtTokenClaims.fromMap(claims)
        return xyz.teodorowicz.pm.model.JwtTokenData(jwtTokenClaims, token)
    }

    /* TODO: implement token revoking */
}
