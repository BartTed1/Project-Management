package xyz.teodorowicz.pm.service

import xyz.teodorowicz.pm.entity.User

abstract class SecurityService {
    /**
     * Verify the provided JWT token.
     *
     * @param token The JWT token to verify.
     * @return True if the token is valid, false otherwise.
     */
    internal abstract fun verifyToken(token: String): Boolean



    /**
     * Compare the provided password with the hashed password.
     *
     * @param password The plain text password to compare.
     * @param hashedPassword The hashed password to compare against.
     * @return True if the passwords match, false otherwise.
     */
    internal abstract fun comparePassword(password: String, hashedPassword: String): Boolean



    /**
     * Generate a JWT token for the user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated JWT token.
     */
    internal abstract fun generateToken(user: User): String



    /**
     * Hash the password using bcrypt algorithm.
     * @param password The plain text password to hash.
     */
    internal abstract fun hashPassword(password: String): String
}