package xyz.teodorowicz.pm.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xyz.teodorowicz.pm.dto.request.LoginRequest
import xyz.teodorowicz.pm.dto.request.RegistrationRequest
import xyz.teodorowicz.pm.dto.response.LoginResponse
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.entity.User

interface AuthController {
    fun verifyToken(
        @RequestHeader("Authorization") authorizationHeader: String
    ): ResponseEntity<Response<Boolean>>

    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Response<LoginResponse>>

    fun register(
        @RequestBody registrationRequest: RegistrationRequest
    ): ResponseEntity<Response<User>>
}