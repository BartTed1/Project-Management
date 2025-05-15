package xyz.teodorowicz.pm.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.dto.request.auth.UpdateUserRequest
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.model.JwtTokenData

interface UserController {

    /**
     * Get all users
     *
     * @param token JWT token
     * @param pageable Pagination information
     * @return ResponseEntity with a page of users
     */
    fun getUsers(
        @JwtToken token: JwtTokenData?,
        pageable: Pageable
    ): ResponseEntity<Page<User>>




    /**
     * Get user by id
     *
     * @param token JWT token
     * @param id User id
     * @return ResponseEntity with user
     */
    fun getUser(
        @JwtToken token: JwtTokenData?,
        @PathVariable("id") id: Long
    ): ResponseEntity<User>



    /**
     * Get user by email
     *
     * @param token JWT token
     * @param email User email
     * @return ResponseEntity with user
     */
    fun getUserByEmail(
        @JwtToken token: JwtTokenData?,
        @Param("email") email: String
    ): ResponseEntity<User>



    /**
     * Update user
     *
     * @param token JWT token
     * @param body User data - all optional, only fields to update
     */
    fun updateUser(
        @JwtToken token: JwtTokenData?,
        @PathVariable("id") id: Long,
        @RequestBody body: UpdateUserRequest,
    ): ResponseEntity<User>



    /**
     * Delete user
     *
     * @param token JWT token
     * @param id User id
     */
    fun deleteUser(
        @JwtToken token: JwtTokenData?,
        @PathVariable("id") id: Long
    ): ResponseEntity<Unit>

}