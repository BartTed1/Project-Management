package xyz.teodorowicz.pm

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.check
import org.mockito.kotlin.anyOrNull
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import org.mockito.kotlin.verify
import xyz.teodorowicz.pm.repository.UserRepository
import xyz.teodorowicz.pm.service.SecurityService
import xyz.teodorowicz.pm.service.AuthServiceImpl
import xyz.teodorowicz.pm.entity.User
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class AuthServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var securityService: SecurityService

    @InjectMocks
    lateinit var authService: AuthServiceImpl

    @Test
    fun `should register new user`() {
        // given
        val email = "john@example.com"
        val rawPassword = "1234"
        val hashedPassword = "hashed1234"
        val firstName = "John"
        val lastName = "Doe"
        val login = "johndoe"

        val expectedUser = User(name = "$firstName $lastName", email = email, password = hashedPassword, role = SystemRole.USER)

        whenever(securityService.hashPassword(rawPassword)).thenReturn(hashedPassword)
        whenever(userRepository.existsByEmail(email)).thenReturn(false)
        whenever(userRepository.save(any<User>())).thenReturn(expectedUser)

        // when
        val result = authService.register(email, rawPassword, firstName, lastName, login)

        // then
        assertEquals(expectedUser.email, result.email)
        assertEquals(expectedUser.password, result.password)
        verify(userRepository).save(any())
    }
    @Test
    fun `should login successfully with correct credentials`() {
        // given
        val email = "john@example.com"
        val rawPassword = "1234"
        val hashedPassword = "hashed1234"
        val user = User(name = "John Doe", email = email, password = hashedPassword, role = SystemRole.USER)

        whenever(userRepository.findByEmail(email)).thenReturn(user)
        whenever(securityService.comparePassword(rawPassword, hashedPassword)).thenReturn(true)

        // when
        val result = authService.login(email, rawPassword)

        // then
        assertEquals(user, result)
        verify(userRepository).findByEmail(email)
        verify(securityService).comparePassword(rawPassword, hashedPassword)
    }
    @Test
    fun `should throw UnauthorizedException when email not found`() {
        // given
        val email = "notfound@example.com"
        val password = "whatever"

        whenever(userRepository.findByEmail(email)).thenReturn(null)

        // when / then
        val exception = kotlin.runCatching {
            authService.login(email, password)
        }.exceptionOrNull()

        assert(exception is xyz.teodorowicz.pm.exception.UnauthorizedException)
    }
    @Test
    fun `should throw UnauthorizedException when password is incorrect`() {
        // given
        val email = "john@example.com"
        val rawPassword = "wrong"
        val hashedPassword = "hashed1234"
        val user = User(name = "John Doe", email = email, password = hashedPassword, role = SystemRole.USER)

        whenever(userRepository.findByEmail(email)).thenReturn(user)
        whenever(securityService.comparePassword(rawPassword, hashedPassword)).thenReturn(false)

        // when / then
        val exception = kotlin.runCatching {
            authService.login(email, rawPassword)
        }.exceptionOrNull()

        assert(exception is xyz.teodorowicz.pm.exception.UnauthorizedException)
    }
    @Test
    fun `should throw BadRequestException when email already exists`() {
        // given
        val email = "john@example.com"
        val rawPassword = "1234"
        val firstName = "John"
        val lastName = "Doe"
        val login = "johndoe"

        whenever(userRepository.existsByEmail(email)).thenReturn(true)

        // when / then
        val exception = kotlin.runCatching {
            authService.register(email, rawPassword, firstName, lastName, login)
        }.exceptionOrNull()

        assert(exception is xyz.teodorowicz.pm.exception.BadRequestException)
    }
    @Test
    fun `should throw BadRequestException and not proceed when email already exists`() {
        // given
        val email = "john@example.com"
        val rawPassword = "1234"
        val firstName = "John"
        val lastName = "Doe"
        val login = "johndoe"

        whenever(userRepository.existsByEmail(email)).thenReturn(true)

        // when / then
        val exception = kotlin.runCatching {
            authService.register(email, rawPassword, firstName, lastName, login)
        }.exceptionOrNull()

        assert(exception is xyz.teodorowicz.pm.exception.BadRequestException)

        verify(userRepository).existsByEmail(email)
        verifyNoMoreInteractions(userRepository, securityService)
    }
    @Test
    fun `should return true when users exist`() {
        whenever(userRepository.count()).thenReturn(5L)
        assertEquals(true, authService.isAnyUserRegistered())
    }

    @Test
    fun `should return false when no users exist`() {
        whenever(userRepository.count()).thenReturn(0L)
        assertEquals(false, authService.isAnyUserRegistered())
    }

}


