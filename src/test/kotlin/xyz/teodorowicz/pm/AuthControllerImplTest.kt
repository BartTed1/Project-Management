package xyz.teodorowicz.pm.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import xyz.teodorowicz.pm.dto.request.auth.LoginRequest
import xyz.teodorowicz.pm.dto.request.auth.RegistrationRequest
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.service.AuthService
import xyz.teodorowicz.pm.service.SecurityService

@WebMvcTest(AuthControllerImpl::class)
class AuthControllerImplTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var authService: AuthService

    @MockBean
    lateinit var securityService: SecurityService

    private fun testUser() = User(
        id = 1L,
        email = "john@example.com",
        name = "John Doe",
        password = "hashed",
        role = SystemRole.USER
    )

    @Test
    fun `should login user and return token`() {
        val loginRequest = LoginRequest(email = "john@example.com", password = "secret")
        val user = testUser()
        val fakeToken = "fake-jwt-token"

        Mockito.`when`(authService.login("john@example.com", "secret")).thenReturn(user)
        Mockito.`when`(securityService.generateToken(user)).thenReturn(fakeToken)

        mockMvc.perform(
            post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.token").value(fakeToken))
            .andExpect(jsonPath("$.data.user.email").value("john@example.com"))
    }

    @Test
    fun `should register user and return response`() {
        val registrationRequest = RegistrationRequest(
            email = "john@example.com",
            password = "password123",
            firstName = "John",
            lastName = "Doe",
            login = "johndoe"
        )

        val user = testUser()

        Mockito.`when`(authService.register(
            email = registrationRequest.email,
            password = registrationRequest.password,
            firstName = registrationRequest.firstName,
            lastName = registrationRequest.lastName,
            login = registrationRequest.login
        )).thenReturn(user)

        mockMvc.perform(
            post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.email").value("john@example.com"))
    }

    @Test
    fun `should verify token and return true`() {
        val token = "faketoken"
        val claims = mapOf(
            "userId" to 1L,
            "email" to "john@example.com",
            "role" to "USER"
        )

        Mockito.`when`(securityService.verifyToken(token)).thenReturn(true)
        Mockito.`when`(securityService.getTokenClaims(token)).thenReturn(claims)

        mockMvc.perform(
            get("/auth")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("true"))
    }
}
