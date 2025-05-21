package xyz.teodorowicz.pm.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.annotation.SystemRole
import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.enumeration.SortDirection
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.exception.InternalServerErrorException
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.repository.ProjectRepository
import xyz.teodorowicz.pm.service.ProjectService
import xyz.teodorowicz.pm.enumeration.user.SystemRole as SystemRoleEnum

@RestController
@RequestMapping("projects")
@Tag(name = "Project API", description = "API for project management")
@CrossOrigin(origins = ["*"])
class ProjectControllerImpl(
    private val projectService: ProjectService,
    private val projectRepository: ProjectRepository
) : ProjectController {

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project with the given details.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Project created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid project details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        ]
    )
    override fun createProject(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project details", description = "Details of the project to be created")
        @RequestBody body: CreateProjectRequest
    ): ResponseEntity<Project> {
        val project = projectService.createProject(token, body)
        return ResponseEntity.status(HttpStatus.CREATED).body(project)
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by ID", description = "Retrieves the project with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    override fun getProject(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to be retrieved")
        @PathVariable("projectId")
        projectId: String
    ): ResponseEntity<Project> {
        val project = projectService.getProject(projectId)
        return ResponseEntity.ok(project)
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update a new project", description = "Update a new project")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Project updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid project details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    override fun updateProject(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to be updated")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Project details", description = "Details of the project to be updated")
        @RequestBody
        request: UpdateProjectRequest
    ): ResponseEntity<Project> {
        val project = projectService.updateProject(projectId, request)
        return ResponseEntity.ok(project)
    }

    @GetMapping
    @Operation(summary = "Get projects", description = "Retrieves a paginated project")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "project retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
        ]
    )
    override fun getProjects(@Parameter(description = "JWT token")
     @JwtToken
     token: JwtTokenData?,

    @Parameter(description = "Pagination information", example = "page=0&size=10")
     pageable: Pageable
    ): ResponseEntity<Page<Project>> {
        return try {
            val projects = projectRepository.findAll(pageable)
            ResponseEntity.ok(projects)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

//    @GetMapping
//    @Operation(summary = "List projects", description = "Retrieves a list of projects based on the provided filters.")
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
//            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
//            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
//        ]
//    )
//    fun listProjects(
//        @Parameter(description = "JWT token")
//        @JwtToken token: JwtTokenData?,
//
//        @Parameter(name = "Search query", description = "Query to filter projects by name, description or usernames")
//        @RequestParam("query")
//        query: String?,
//
//        @Parameter(name = "Page number", description = "Page number for pagination")
//        @RequestParam("page")
//        page: Int,
//
//        @Parameter(name = "Page size", description = "Number of projects per page")
//        @RequestParam("size")
//        size: Int,
//
//        @Parameter(name = "Project status", description = "Status of the projects to filter by")
//        @RequestParam("status")
//        status: ProjectStatus?,
//
//        @Parameter(name = "Project priority", description = "Priority of the projects to filter by")
//        @RequestParam("priority")
//        priority: ProjectPriority?,
//
//        @Parameter(name = "Sort by", description = "Field to sort the projects by")
//        @RequestParam("sortBy")
//        sortBy: String?,
//
//        @Parameter(name = "Sort direction", description = "Direction to sort the projects (ascending or descending)")
//        @RequestParam("sortDirection")
//        sortDirection: SortDirection?,
//    ): ResponseEntity<List<Project>> {
//        val projects = projectService.listProjects(query, page, size, status, priority, sortBy, sortDirection)
//        return ResponseEntity.ok(projects)
//    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete a project", description = "Deletes the project with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @SystemRole(SystemRoleEnum.ADMIN)
    override fun deleteProject(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to be deleted")
        @PathVariable("projectId")
        projectId: String
    ): ResponseEntity<Unit> {
        projectService.deleteProject(token, projectId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{projectId}/role")
    @Operation(summary = "Create a new project role", description = "Creates a new role for the specified project.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Project role created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid role details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @SystemRole(SystemRoleEnum.ADMIN)
    override fun createProjectRole(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to create the role for")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Role name", description = "Name of the role to be created")
        @RequestBody
        roleName: String
    ): ResponseEntity<Unit> {
        projectService.createProjectRole(token, projectId, roleName)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }





    @DeleteMapping("/{projectId}/role")
    @Operation(summary = "Delete a project role", description = "Deletes the specified role from the project.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Project role deleted successfully"),
            ApiResponse(responseCode = "400", description = "Invalid role details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    @SystemRole(SystemRoleEnum.ADMIN)
    override fun deleteProjectRole(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to delete the role from")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Role name", description = "Name of the role to be deleted")
        @RequestBody
        roleName: String
    ): ResponseEntity<Unit> {
        projectService.deleteProjectRole(token, projectId, roleName)
        return ResponseEntity.noContent().build()
    }





    @PostMapping("/{projectId}/users")
    @Operation(summary = "Assign users to a project", description = "Assigns users to the specified project with a specific role.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Users assigned to project successfully"),
            ApiResponse(responseCode = "400", description = "Invalid assignment details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project or users not found")
        ]
    )
    override fun assignUsersToProject(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to assign the users to")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Assignment details", description = "Details of the users to be assigned and their roles")
        @RequestBody
        assignUsersToProjectRequest: AssignUserToProjectRequest
    ): ResponseEntity<Unit> {
        projectService.assignUsersToProject(token, projectId, assignUsersToProjectRequest)
        return ResponseEntity.ok().build()
    }




    @DeleteMapping("/{projectId}/users")
    @Operation(summary = "Remove users from a project", description = "Removes the specified users from the project.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Users removed from project successfully"),
            ApiResponse(responseCode = "400", description = "Invalid removal details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project or users not found")
        ]
    )
    @SystemRole(SystemRoleEnum.ADMIN)
    override fun removeUserFromProject(
        @Parameter(description = "JWT token")
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Project ID", description = "ID of the project to remove the users from")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "User IDs", description = "List of user IDs to be removed from the project")
        @RequestBody
        userIds: List<Long>
    ): ResponseEntity<Unit> {
        projectService.removeUserFromProject(token, projectId, userIds)
        return ResponseEntity.ok().build()
    }
}