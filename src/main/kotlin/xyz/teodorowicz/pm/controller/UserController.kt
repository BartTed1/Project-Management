package xyz.teodorowicz.pm.controller

import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import xyz.teodorowicz.pm.dto.request.UpdateUserRequest
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.dto.response.UserResponse

interface UserController {

    /**
     * Get all users
     *
     * @param authorizationHeader Authorization header
     * @param page Page number
     * @param size Page size
     * @return ResponseEntity with a list of users
     */
    fun getUsers(
        @RequestHeader("Authorization") authorizationHeader: String,
        @Param("page") page: Int,
        @Param("size") size: Int,
    ): ResponseEntity<Response<Array<UserResponse>>>



    /**
     * Get user by id
     *
     * @param authorizationHeader Authorization header
     * @param id User id
     * @return ResponseEntity with user
     */
    fun getUser(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable("id") id: Long
    ): ResponseEntity<Response<UserResponse?>>



    /**
     * Get user by email
     *
     * @param authorizationHeader Authorization header
     * @param email User email
     * @return ResponseEntity with user
     */
    fun getUserByEmail(
        @RequestHeader("Authorization") authorizationHeader: String,
        @Param("email") email: String
    ): ResponseEntity<Response<UserResponse?>>



    /**
     * Update user
     *
     * @param authorizationHeader Authorization header
     * @param body User data - all optional, only fields to update
     */
    fun updateUser(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestBody body: UpdateUserRequest
    ): ResponseEntity<Response<UserResponse?>>



    /**
     * Delete user
     *
     * @param authorizationHeader Authorization header
     * @param id User id
     * @return If user was deleted
     */
    fun deleteUser(
        @RequestHeader("Authorization") authorizationHeader: String,
        @Param("id") id: Long
    ): ResponseEntity<Response<Boolean>>
}