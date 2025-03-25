package xyz.teodorowicz.pm.controller

import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.dto.request.UpdateUserRequest
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.dto.response.UserResponse

@RestController
@RequestMapping("user")
class UserControllerImpl : UserController {

    @GetMapping
    override fun getUsers(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam page: Int,
        size: Int
    ): ResponseEntity<Response<Array<UserResponse>>> {
        TODO("Not yet implemented")
    }

    @GetMapping("/{id}")
    override fun getUser(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable id: Long
    ): ResponseEntity<Response<UserResponse?>> {
        TODO("Not yet implemented")
    }


    override fun getUserByEmail(authorizationHeader: String, email: String): ResponseEntity<Response<UserResponse?>> {
        TODO("Not yet implemented")
    }

    override fun updateUser(
        authorizationHeader: String,
        body: UpdateUserRequest
    ): ResponseEntity<Response<UserResponse?>> {
        TODO("Not yet implemented")
    }

    override fun deleteUser(authorizationHeader: String, id: Long): ResponseEntity<Response<Boolean>> {
        TODO("Not yet implemented")
    }
}