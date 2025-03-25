package xyz.teodorowicz.pm.service

import xyz.teodorowicz.pm.dto.request.UpdateUserRequest
import xyz.teodorowicz.pm.entity.User

abstract class UserService {
    /**
     * Get all users
     *
     * @param page Page number
     * @param size Page size
     * @return List of users
     */
    abstract fun getUsers(page: Int, size: Int): List<User>



    /**
     * Get user by id
     *
     * @param id User id
     * @return User
     */
    abstract fun getUser(id: Long): User?



    /**
     * Get user by email
     *
     * @param email User email
     * @return User
     */
    abstract fun getUserByEmail(email: String): User?



    /**
     * Update user
     *
     * @param id User id
     * @param request Update user request
     * @return Updated user
     */
    abstract fun updateUser(id: Long, request: UpdateUserRequest): User


    /**
     * Delete user
     *
     * @param id User id
     * @return If user was deleted
     */
    abstract fun deleteUser(id: Long): Boolean
}