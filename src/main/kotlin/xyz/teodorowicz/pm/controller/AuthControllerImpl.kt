package xyz.teodorowicz.pm.controller

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.dto.request.LoginRequest
import xyz.teodorowicz.pm.dto.request.RegistrationRequest
import xyz.teodorowicz.pm.dto.response.LoginResponse
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.dto.response.UserResponse
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.service.AuthService
import xyz.teodorowicz.pm.service.SecurityService

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication API", description = "API for user authentication")
class AuthControllerImpl(
    private val authService: AuthService,
    private val securityService: SecurityService
) : AuthController {

    @GetMapping
    @Operation(summary = "Verify token", description = "Checks if the provided token is valid")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Token is valid"),
            ApiResponse(responseCode = "401", description = "Token is invalid")
        ]
    )

    override fun verifyToken(
        @Parameter(description = "Authorization header with Bearer token")
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Boolean> {
        val token = authorizationHeader.replace("Bearer ", "")
        val isValid = securityService.verifyToken(token)

        return if (isValid) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Logs in a user and returns a token")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User logged in successfully"),
            ApiResponse(responseCode = "401", description = "Invalid credentials")
        ]
    )
    override fun login(
        @RequestBody @Parameter(description = "Login request") loginRequest: LoginRequest
    ): ResponseEntity<Response<LoginResponse>> {
        val user = authService.login(loginRequest.email, loginRequest.password)
        val token = securityService.generateToken(user)

        return ResponseEntity.ok(
            Response(
                status = 200,
                message = "Zalogowano pomyślnie",
                data = LoginResponse(
                    token = token,
                    user = user
                )
            )
        )
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Registers a new user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered successfully"),
            ApiResponse(responseCode = "400", description = "Invalid request")
        ]
    )
    override fun register(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @RequestBody @Parameter(description = "Registration request") registrationRequest: RegistrationRequest
    ): ResponseEntity<Response<User?>> {

        // Check if the user is already registered
        if (authService.isAnyUserRegistered()) {
            val role = authorizationHeader
                ?.removePrefix("Bearer ")
                ?.let(securityService::getRoleFromToken)
                ?.lowercase()

            if (role?.contains("admin") != true) {
                return ResponseEntity.badRequest().body(
                    Response(
                        status = 400,
                        message = "Rejestracja jest możliwa jedynie przez konto administratora",
                        data = null
                    )
                )
            }
        }

        val user = authService.register(
            email = registrationRequest.email,
            password = registrationRequest.password,
            firstName = registrationRequest.firstName,
            lastName = registrationRequest.lastName,
            login = registrationRequest.login
        )

        return ResponseEntity.ok(
            Response(
                status = 201,
                message = "Zarejestrowano pomyślnie",
                data = user
            )
        )
    }
}
