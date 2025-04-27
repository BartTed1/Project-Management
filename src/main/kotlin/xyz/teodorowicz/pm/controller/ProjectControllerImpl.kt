package xyz.teodorowicz.pm.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.enumeration.SortDirection
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus

@RestController
@RequestMapping("projects")
@Tag(name = "Project API", description = "API for project management")
@CrossOrigin(origins = ["*"])
class ProjectControllerImpl : ProjectController {

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
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project details", description = "Details of the project to be created")
        @RequestBody
        request: CreateProjectRequest
    ): ResponseEntity<Project> {
        TODO("Not yet implemented")
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
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to be retrieved")
        @PathVariable("projectId")
        projectId: String
    ): ResponseEntity<Project> {
        TODO("Not yet implemented")
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
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to be updated")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Project details", description = "Details of the project to be updated")
        @RequestBody
        request: UpdateProjectRequest
    ): ResponseEntity<Project> {
        TODO("Not yet implemented")
    }

    @GetMapping
    @Operation(summary = "List projects", description = "Retrieves a list of projects based on the provided filters.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        ]
    )
    override fun listProjects(
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Search query", description = "Query to filter projects by name, description or usernames")
        @Param("query")
        query: String?,

        @Parameter(name = "Page number", description = "Page number for pagination")
        @Param("page")
        page: Int,

        @Parameter(name = "Page size", description = "Number of projects per page")
        @Param("size")
        size: Int,

        @Parameter(name = "Project status", description = "Status of the projects to filter by")
        @Param("status")
        status: ProjectStatus?,

        @Parameter(name = "Project priority", description = "Priority of the projects to filter by")
        @Param("priority")
        priority: ProjectPriority?,

        @Parameter(name = "Sort by", description = "Field to sort the projects by")
        @Param("sortBy")
        sortBy: String?,

        @Parameter(name = "Sort direction", description = "Direction to sort the projects (ascending or descending)")
        @Param("sortDirection")
        sortDirection: SortDirection?,
    ): ResponseEntity<List<Project>> {
        TODO("Not yet implemented")
    }

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
    override fun deleteProject(
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to be deleted")
        @PathVariable("projectId")
        projectId: String
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
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
    override fun createProjectRole(
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to create the role for")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Role name", description = "Name of the role to be created")
        @RequestBody
        roleName: String
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
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
    override fun deleteProjectRole(
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to delete the role from")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Role name", description = "Name of the role to be deleted")
        @RequestBody
        roleName: String
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
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
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to assign the users to")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "Assignment details", description = "Details of the users to be assigned and their roles")
        @RequestBody
        assignUserToProjectRequest: AssignUserToProjectRequest
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
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
    override fun removeUserFromProject(
        @Parameter(name = "Authorization header with Bearer token")
        @RequestHeader("Authorization")
        authorizationHeader: String?,

        @Parameter(name = "Project ID", description = "ID of the project to remove the users from")
        @PathVariable("projectId")
        projectId: String,

        @Parameter(name = "User IDs", description = "List of user IDs to be removed from the project")
        @RequestBody
        userIds: List<Long>
    ): ResponseEntity<Unit> {
        TODO("Not yet implemented")
    }
}