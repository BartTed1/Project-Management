package xyz.teodorowicz.pm

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.service.SecurityServiceImpl
import java.util.*

class SecurityServiceImplTest {

    private lateinit var securityService: SecurityServiceImpl
    private val secretKey = "abcdefghijklmnopqrstuvwxyz123456" // 32 znaki
    private val expirationMillis = 3600000L // 1 hour

    @BeforeEach
    fun setup() {
        val encoder = BCryptPasswordEncoder()
        securityService = SecurityServiceImpl(encoder, secretKey, expirationMillis)
    }

    @Test
    fun `should hash password`() {
        val raw = "password123"
        val hashed = securityService.hashPassword(raw)

        assertNotEquals(raw, hashed)
        assertTrue(securityService.comparePassword(raw, hashed))
    }

    @Test
    fun `should compare password correctly`() {
        val raw = "secret"
        val hash = securityService.hashPassword(raw)

        assertTrue(securityService.comparePassword(raw, hash))
        assertFalse(securityService.comparePassword("wrong", hash))
    }

    @Test
    fun `should generate and verify token`() {
        val user = User(id = 1L, email = "a@a.com", name = "A", password = "x", role = SystemRole.ADMIN)
        val token = securityService.generateToken(user)

        assertTrue(securityService.verifyToken(token))
    }

    @Test
    fun `should extract claims from token`() {
        val user = User(id = 42L, email = "user@example.com", name = "User", password = "pwd", role = SystemRole.USER)
        val token = securityService.generateToken(user)

        val claims = securityService.getTokenClaims(token)

        assertEquals(42L, (claims["userId"] as Number).toLong())
        assertEquals("user@example.com", claims["email"])
        assertEquals("USER", claims["role"])
    }

    @Test
    fun `should extract role from token`() {
        val user = User(id = 2L, email = "x@x.com", name = "X", password = "pwd", role = SystemRole.ADMIN)
        val token = securityService.generateToken(user)

        val role = securityService.getRoleFromToken(token)
        assertEquals("ADMIN", role)
    }

    @Test
    fun `should reject invalid token`() {
        val invalidToken = "bad.token.string"

        assertFalse(securityService.verifyToken(invalidToken))
        assertThrows(RuntimeException::class.java) {
            securityService.getRoleFromToken(invalidToken)
        }
    }
}
