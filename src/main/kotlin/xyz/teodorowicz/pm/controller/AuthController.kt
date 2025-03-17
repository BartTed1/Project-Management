package xyz.teodorowicz.pm.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xyz.teodorowicz.pm.dto.request.LoginRequest
import xyz.teodorowicz.pm.dto.request.RegistrationRequest
import xyz.teodorowicz.pm.dto.response.LoginResponse
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.dto.response.UserResponse

interface AuthController {

    /**
     * Verifies the provided token.
     *
     * @param authorizationHeader The authorization header containing the token.
     * @return A response entity containing the verification result.
     */
    fun verifyToken(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Response<Boolean>>



    /**
     * Logs in a user and returns a token.
     *
     * @param loginRequest The login request containing the user's credentials.
     * @return A response entity containing the login result.
     */
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Response<LoginResponse>>



    /**
     * Registers a new user.
     *
     * Registration without token is possible only when there are no users in the database.
     *
     * @param authorizationHeader The authorization header containing the token.
     * @param registrationRequest The registration request containing the user's details.
     * @return A response entity containing the registration result.
     */
    fun register(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<Response<UserResponse?>>
}