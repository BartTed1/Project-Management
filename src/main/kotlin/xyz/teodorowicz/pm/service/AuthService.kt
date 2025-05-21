package xyz.teodorowicz.pm.service

import xyz.teodorowicz.pm.entity.User

abstract class AuthService {
    /**
     * Login the user with the provided credentials.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return The logged-in user.
     */
    abstract fun login(
        email: String,
        password: String,
    ): User



    /**
     * Register a new user with the provided details.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @param firstName The first name of the user.
     * @param lastName The last name of the user.
     * @param login The login of the user.
     * @return The registered user.
     */
    abstract fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        login: String,
    ): User


    /**
     * Check if any user is registered.
     *
     * This is used to determine if registration is open or closed.
     * @return true if any user is registered, false otherwise.
     */
    abstract fun isAnyUserRegistered(): Boolean
}