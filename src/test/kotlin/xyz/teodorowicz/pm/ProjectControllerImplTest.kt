package xyz.teodorowicz.pm.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import xyz.teodorowicz.pm.config.TestWebConfig
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.model.JwtTokenClaims
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.repository.ProjectRepository
import xyz.teodorowicz.pm.service.ProjectService
import xyz.teodorowicz.pm.service.SecurityService
import java.time.LocalDate

@WebMvcTest(ProjectControllerImpl::class)
@Import(TestWebConfig::class) // ⬅️ To jest KLUCZOWE dla rejestracji JwtTokenArgumentResolver
class ProjectControllerImplTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var projectService: ProjectService

    @MockBean
    lateinit var projectRepository: ProjectRepository

    @MockBean
    lateinit var securityService: SecurityService

    private fun jwt() = "test.jwt.token"

    private fun testUser() = User(1L, "email", "name", "pass", SystemRole.USER)

    private fun project() = Project(
        id = 1L,
        name = "Project",
        description = "Desc",
        plannedStartDate = LocalDate.now(),
        plannedEndDate = LocalDate.now().plusDays(10),
        status = ProjectStatus.NOT_STARTED,
        priority = ProjectPriority.NORMAL,
        owner = testUser(),
        users = mutableSetOf(testUser()),
        roles = emptySet()
    )

    @Test
    fun `should create project`() {
        val request = CreateProjectRequest("Project")
        val jwtToken = "test.jwt.token"
        val claims = JwtTokenClaims(userId = 1L, email = "email", role = xyz.teodorowicz.pm.enumeration.user.SystemRole.USER)
        val tokenData = JwtTokenData(claims = claims, token = jwtToken)

        `when`(securityService.verifyToken(jwtToken)).thenReturn(true)
        `when`(securityService.getTokenClaims(jwtToken)).thenReturn(
            mapOf("userId" to 1L, "email" to "email", "role" to "USER")
        )
        `when`(projectService.createProject(tokenData, request)).thenReturn(project())

        mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $jwtToken")
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Project"))
    }

    @Test
    fun `should return project by id`() {
        val token = jwt()

        `when`(securityService.verifyToken(token)).thenReturn(true)
        `when`(securityService.getTokenClaims(token)).thenReturn(
            mapOf("userId" to 1L, "email" to "email", "role" to "USER")
        )
        `when`(projectService.getProject("1")).thenReturn(project())

        mockMvc.perform(
            get("/projects/1")
                .header("Authorization", "Bearer $token")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
    }

    @Test
    fun `should update project`() {
        val token = jwt()
        val update = UpdateProjectRequest(name = "Updated")

        `when`(securityService.verifyToken(token)).thenReturn(true)
        `when`(securityService.getTokenClaims(token)).thenReturn(
            mapOf("userId" to 1L, "email" to "email", "role" to "USER")
        )
        `when`(projectService.updateProject("1", update)).thenReturn(project().copy(name = "Updated"))

        mockMvc.perform(
            put("/projects/1")
                .header("Authorization", "Bearer $token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Updated"))
    }
    //add delete project, create project role, delete project role, assign users to project, should remove users from project
}
