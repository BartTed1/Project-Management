package xyz.teodorowicz.pm.service

import xyz.teodorowicz.pm.entity.User

abstract class SecurityService {
    /**
     * Parse the JWT token and return its claims.
     *
     * @param token The JWT token to parse.
     * @return Map of claims from the token.
     */
    internal abstract fun getTokenClaims(token: String): Map<String, Any>

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



    /**
     * Get role from token.
     *
     * @param token The JWT token to verify.
     * @return The role of the user.
     */
    abstract fun getRoleFromToken(token: String): String

    /**
     * Parse the JWT token and return token data.
     *
     * @param token The JWT token to parse.
     * @return The parsed JWT token data.
     */
    abstract fun parseJwtToken(token: String): xyz.teodorowicz.pm.model.JwtTokenData
}
