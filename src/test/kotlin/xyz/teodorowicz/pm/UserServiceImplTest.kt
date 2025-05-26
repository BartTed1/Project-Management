package xyz.teodorowicz.pm

import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import xyz.teodorowicz.pm.dto.request.auth.UpdateUserRequest
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.repository.UserRepository
import xyz.teodorowicz.pm.service.UserServiceImpl
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserServiceImpl

    private fun exampleUser(
        id: Long = 1L,
        email: String = "user@example.com",
        name: String = "User"
    ) = User(
        id = id,
        email = email,
        name = name,
        password = "hashed",
        role = SystemRole.USER
    )

    @Test
    fun `should return paginated users`() {
        val users = listOf(exampleUser(1), exampleUser(2))
        val page = PageImpl(users)
        whenever(userRepository.findAll(PageRequest.of(0, 2))).thenReturn(page)

        val result = userService.getUsers(0, 2)

        assertEquals(2, result.size)
        assertEquals("user@example.com", result[0].email)
    }

    @Test
    fun `should return user by id`() {
        val user = exampleUser(10L)
        whenever(userRepository.findById(10L)).thenReturn(Optional.of(user))

        val result = userService.getUser(10L)

        assertNotNull(result)
        assertEquals(10L, result?.id)
    }

    @Test
    fun `should return null if user not found by id`() {
        whenever(userRepository.findById(any())).thenReturn(Optional.empty())

        val result = userService.getUser(99L)

        assertNull(result)
    }

    @Test
    fun `should return user by email`() {
        val user = exampleUser()
        whenever(userRepository.findByEmail("user@example.com")).thenReturn(user)

        val result = userService.getUserByEmail("user@example.com")

        assertNotNull(result)
        assertEquals("user@example.com", result?.email)
    }

    @Test
    fun `should update user when email is not taken`() {
        val user = exampleUser()
        val updatedRequest = UpdateUserRequest(name = "Updated", email = "new@example.com")

        whenever(userRepository.findById(1L)).thenReturn(Optional.of(user))
        whenever(userRepository.findByEmail("new@example.com")).thenReturn(null)
        whenever(userRepository.save(any<User>())).thenAnswer { it.arguments[0] as User }

        val result = userService.updateUser(1L, updatedRequest)

        assertEquals("Updated", result.name)
        assertEquals("new@example.com", result.email)
    }

    @Test
    fun `should throw if email is already taken by another user`() {
        val user = exampleUser()
        val updatedRequest = UpdateUserRequest(name = "Test", email = "taken@example.com")

        whenever(userRepository.findById(1L)).thenReturn(Optional.of(user))
        whenever(userRepository.findByEmail("taken@example.com")).thenReturn(exampleUser(2L, "taken@example.com"))

        val exception = assertThrows<IllegalArgumentException> {
            userService.updateUser(1L, updatedRequest)
        }

        assertEquals("Email jest już zajęty", exception.message)
    }

    @Test
    fun `should throw if user not found on update`() {
        whenever(userRepository.findById(999L)).thenReturn(Optional.empty())

        val request = UpdateUserRequest(name = "Name", email = "email@example.com")

        assertThrows<EntityNotFoundException> {
            userService.updateUser(999L, request)
        }
    }

    @Test
    fun `should delete user successfully`() {
        doNothing().whenever(userRepository).deleteById(1L)

        val result = userService.deleteUser(1L)

        assertTrue(result)
        verify(userRepository).deleteById(1L)
    }

    @Test
    fun `should return false if delete fails`() {
        doThrow(RuntimeException("fail")).whenever(userRepository).deleteById(99L)

        val result = userService.deleteUser(99L)

        assertFalse(result)
    }
}
