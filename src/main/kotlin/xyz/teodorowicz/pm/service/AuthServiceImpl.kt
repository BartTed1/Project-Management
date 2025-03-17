package xyz.teodorowicz.pm.service

import org.springframework.stereotype.Service
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.exception.UnauthorizedException
import xyz.teodorowicz.pm.exception.BadRequestException
import xyz.teodorowicz.pm.exception.NotFoundException
import xyz.teodorowicz.pm.repository.UserRepository

@Service
class AuthServiceImpl(
    private val securityService: SecurityService,
    private val userRepository: UserRepository,
    /* TODO: private val emailService: EmailService */
) : AuthService() {

    override fun login(email: String, password: String): User {
        val user = userRepository.findByEmail(email)
            ?: throw UnauthorizedException("Nieprawidłowy email lub hasło")

        if (!securityService.comparePassword(password, user.password)) {
            throw UnauthorizedException("Nieprawidłowy email lub hasło")
        }

        return user
    }

    override fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        login: String
    ): User {
        if (userRepository.existsByEmail(email)) {
            throw BadRequestException("Użytkownik z tym adresem email już istnieje")
        }

        val hashedPassword = securityService.hashPassword(password)

        val user = User(
            name = "$firstName $lastName",
            email = email,
            password = hashedPassword,
            role = "USER"
        )

        return userRepository.save(user)
    }

    override fun resetPassword(email: String, password: String): User {
        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException("Nie znaleziono użytkownika o podanym adresie email")

        val hashedPassword = securityService.hashPassword(password)

        /* TODO: that do nothing, implement */
        val updatedUser = user.copy(password = hashedPassword)
        return userRepository.save(updatedUser)
    }

    override fun initiatePasswordReset(email: String) {
        return
    }
}
