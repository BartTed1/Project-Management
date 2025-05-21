package xyz.teodorowicz.pm.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import xyz.teodorowicz.pm.dto.request.auth.UpdateUserRequest
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.repository.UserRepository

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
) : UserService() {

    override fun getUsers(page: Int, size: Int): List<User> {
        val pageable = PageRequest.of(page, size)
        return userRepository.findAll(pageable).content
    }

    override fun getUser(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    override fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun updateUser(id: Long, request: UpdateUserRequest): User {
        val existingUser = userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Nie znaleziono użytkownika o ID: $id") }

        request.email?.let { newEmail ->
            if (newEmail != existingUser.email) {
                userRepository.findByEmail(newEmail)?.let {
                    throw IllegalArgumentException("Email jest już zajęty")
                }
            }
        }

        val updatedUser = existingUser.copy(
            name = request.name ?: existingUser.name,
            email = request.email ?: existingUser.email,
        )

        return userRepository.save(updatedUser)
    }

    override fun deleteUser(id: Long): Boolean {
        return try {
            userRepository.deleteById(id)
            true
        } catch (e: Exception) {
            false
        }
    }
}
