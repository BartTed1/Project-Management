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
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
                .build()
                .parseClaimsJws(token)

            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
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
            .claim("email", user.email)
            .claim("role", user.role)
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
            .compact()
    }

    override fun hashPassword(password: String): String {
        return bCryptPasswordEncoder.encode(password)
    }

    /* TODO: implement token revoking */
}
