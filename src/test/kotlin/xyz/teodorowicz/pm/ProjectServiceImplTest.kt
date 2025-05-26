package xyz.teodorowicz.pm

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import org.mockito.kotlin.argThat
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.exception.ForbiddenException
import xyz.teodorowicz.pm.exception.NotFoundException
import xyz.teodorowicz.pm.model.JwtTokenClaims
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.repository.ProjectRepository
import xyz.teodorowicz.pm.repository.UserRepository
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProjectServiceImplTest {

    @Mock
    lateinit var projectRepository: ProjectRepository

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var projectService: xyz.teodorowicz.pm.service.ProjectServiceImpl

    private fun validToken(userId: Long = 1L) = JwtTokenData(
        claims = JwtTokenClaims(
            userId = userId,
            email = "test@example.com",
            role = SystemRole.USER
        ),
        token = "dummy-token"
    )

    @Test
    fun `should throw when token is null in createProject`() {
        val request = CreateProjectRequest(name = "Test Project")
        val ex = assertThrows<IllegalArgumentException> {
            projectService.createProject(null, request)
        }
        assertEquals("Token cannot be null", ex.message)
    }

    @Test
    fun `should throw when user not found in createProject`() {
        whenever(userRepository.findById(any())).thenReturn(Optional.empty())
        val request = CreateProjectRequest(name = "Test Project")
        assertThrows<IllegalArgumentException> {
            projectService.createProject(validToken(), request)
        }
    }

    @Test
    fun `should create project successfully`() {
        val user = User(
            id = 1L,
            email = "john@example.com",
            name = "John",
            password = "pass",
            role = SystemRole.USER
        )

        whenever(userRepository.findById(any())).thenReturn(Optional.of(user))

        val request = CreateProjectRequest(
            name = "Test Project",
            description = "Opis",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.of(2025, 1, 1),
            plannedEndDate = LocalDate.of(2025, 12, 31)
        )

        // Utworzenie expectedProject z pełnym wypełnieniem wszystkich wymaganych pól
        val expectedProject = Project(
            id = 1L,
            name = request.name,
            description = request.description ?: "",
            status = request.status ?: ProjectStatus.NOT_STARTED,
            priority = request.priority ?: ProjectPriority.NORMAL,
            plannedStartDate = request.plannedStartDate ?: LocalDate.now(),
            plannedEndDate = request.plannedEndDate,
            owner = user,
            users = mutableSetOf(user),
            roles = emptySet()
        )

        whenever(projectRepository.save(any<Project>())).thenReturn(expectedProject)

        val result = projectService.createProject(validToken(), request)

        assertEquals("Test Project", result.name)
        assertEquals(user, result.owner)
    }

    @Test
    fun `should return project by id`() {
        val project = Project(
            id = 1L,
            name = "Example",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = User(id = 99L, email = "dummy@example.com", name = "Dummy", password = "pass", role = SystemRole.USER),
            users = mutableSetOf(),
            roles = emptySet()
        )
        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))
        val result = projectService.getProject("1")
        assertEquals("Example", result.name)
    }

    @Test
    fun `should throw NotFoundException when project not found`() {
        whenever(projectRepository.findById("x")).thenReturn(Optional.empty())
        assertThrows<NotFoundException> {
            projectService.getProject("x")
        }
    }

    @Test
    fun `should update project with provided fields`() {
        val user = User(
            id = 1L,
            email = "john@example.com",
            name = "John",
            password = "pass",
            role = SystemRole.USER
        )

        val existingProject = Project(
            id = 1L,
            name = "Old Name",
            description = "Old desc",
            priority = ProjectPriority.LOW,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.of(2025, 1, 1),
            plannedEndDate = null,
            owner = user,
            users = mutableSetOf(user),
            roles = emptySet()
        )

        whenever(projectRepository.findById("1")).thenReturn(Optional.of(existingProject))

        val expectedUpdatedProject = existingProject.copy(
            name = "New Name",
            description = "New desc"
        )

        whenever(projectRepository.save(any<Project>())).thenReturn(expectedUpdatedProject)

        val request = UpdateProjectRequest(
            name = "New Name",
            description = "New desc"
        )

        val result = projectService.updateProject("1", request)

        assertEquals("New Name", result.name)
        assertEquals("New desc", result.description)
    }

    @Test
    fun `should delete project if user is owner`() {
        val user = User(
            id = 1L,
            email = "",
            name = "",
            password = "",
            role = SystemRole.USER
        )
        val project = Project(
            id = 1L,
            name = "",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = user,
            users = mutableSetOf(user),
            roles = emptySet()
        )
        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(user))

        projectService.deleteProject(validToken(), "1")
        verify(projectRepository).delete(project)
    }

    @Test
    fun `should delete project if user is admin`() {
        val owner = User(
            id = 2L,
            email = "",
            name = "",
            password = "",
            role = SystemRole.USER
        )
        val admin = User(
            id = 1L,
            email = "",
            name = "",
            password = "",
            role = SystemRole.ADMIN
        )
        val project = Project(
            id = 1L,
            name = "",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = owner,
            users = mutableSetOf(owner, admin),
            roles = emptySet()
        )
        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(admin))

        projectService.deleteProject(validToken(), "1")
        verify(projectRepository).delete(project)
    }

    @Test
    fun `should forbid deleting project if user is not owner or admin`() {
        val owner = User(
            id = 2L,
            email = "",
            name = "",
            password = "",
            role = SystemRole.USER
        )
        val otherUser = User(
            id = 1L,
            email = "",
            name = "",
            password = "",
            role = SystemRole.USER
        )
        val project = Project(
            id = 1L,
            name = "",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = owner,
            users = mutableSetOf(owner, otherUser),
            roles = emptySet()
        )
        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(otherUser))

        assertThrows<ForbiddenException> {
            projectService.deleteProject(validToken(), "1")
        }
    }

    @Test
    fun `should add role to project when it does not exist`() {
        val user = User(1L, "john@example.com", "John", "pass", SystemRole.USER)
        val project = Project(
            id = 1L,
            name = "Project",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = user,
            users = mutableSetOf(user),
            roles = emptySet()
        )

        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))

        val updatedProject = project.copy(roles = setOf("Developer"))
        whenever(projectRepository.save(any<Project>())).thenReturn(updatedProject)

        projectService.createProjectRole(validToken(), "1", "Developer")

        verify(projectRepository).save(argThat { roles.contains("Developer") })
    }

    @Test
    fun `should assign users to project`() {
        val user1 = User(1L, "a@example.com", "A", "pass", SystemRole.USER)
        val user2 = User(2L, "b@example.com", "B", "pass", SystemRole.USER)
        val existingProject = Project(
            id = 1L,
            name = "P",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = user1,
            users = mutableSetOf(user1),
            roles = emptySet()
        )

        whenever(projectRepository.findById("1")).thenReturn(Optional.of(existingProject))
        whenever(userRepository.findAllById(listOf(2L))).thenReturn(listOf(user2))
        whenever(projectRepository.save(any<Project>())).thenAnswer { it.arguments[0] }

        val request = AssignUserToProjectRequest(userId = listOf(2L))
        projectService.assignUsersToProject(validToken(), "1", request)

        verify(projectRepository).save(argThat { users.contains(user1) && users.contains(user2) })
    }

    @Test
    fun `should remove users from project`() {
        val user1 = User(1L, "a@example.com", "A", "pass", SystemRole.USER)
        val user2 = User(2L, "b@example.com", "B", "pass", SystemRole.USER)
        val project = Project(
            id = 1L,
            name = "P",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = user1,
            users = mutableSetOf(user1, user2),
            roles = emptySet()
        )

        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))
        whenever(projectRepository.save(any<Project>())).thenAnswer { it.arguments[0] }

        projectService.removeUserFromProject(validToken(), "1", listOf(2L))

        verify(projectRepository).save(argThat { !users.contains(user2) && users.contains(user1) })
    }

    @Test
    fun `should throw when trying to remove nonexistent user from project`() {
        val user = User(1L, "a@example.com", "A", "pass", SystemRole.USER)
        val project = Project(
            id = 1L,
            name = "P",
            description = "",
            priority = ProjectPriority.NORMAL,
            status = ProjectStatus.NOT_STARTED,
            plannedStartDate = LocalDate.now(),
            plannedEndDate = null,
            owner = user,
            users = mutableSetOf(user),
            roles = emptySet()
        )

        whenever(projectRepository.findById("1")).thenReturn(Optional.of(project))

        val exception = assertThrows<IllegalArgumentException> {
            projectService.removeUserFromProject(validToken(), "1", listOf(999L))
        }

        assertEquals("No users found in the project with the provided IDs", exception.message)
    }

}
