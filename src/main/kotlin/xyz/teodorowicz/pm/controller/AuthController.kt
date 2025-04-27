package xyz.teodorowicz.pm.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.dto.request.auth.LoginRequest
import xyz.teodorowicz.pm.dto.request.auth.RegistrationRequest
import xyz.teodorowicz.pm.dto.response.LoginResponse
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.entity.User

interface AuthController {

    /**
     * Verifies the provided token.
     *
     * @param token The JWT token.
     * @return A response entity containing the verification result.
     */
    fun verifyToken(
        @JwtToken token: JwtTokenData
    ): ResponseEntity<Boolean>



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
     * @param token The JWT token.
     * @param registrationRequest The registration request containing the user's details.
     * @return A response entity containing the registration result.
     */
    fun register(
        @JwtToken token: JwtTokenData?,
        @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<Response<User?>>
}
