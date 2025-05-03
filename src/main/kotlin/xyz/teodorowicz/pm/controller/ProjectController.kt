package xyz.teodorowicz.pm.controller

import org.springframework.data.repository.query.Param
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.enumeration.SortDirection
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.model.JwtTokenData

interface ProjectController {

    /**
     * Creates a new project with the given details.
     *
     * @param token JWT token
     * @param body The details of the project to be created.
     * @return The created project.
     */
    fun createProject(
        @JwtToken token: JwtTokenData?,
        @RequestBody body: CreateProjectRequest
    ): ResponseEntity<Project>

    /**
     * Retrieves the details of a project by its ID.
     *
     * @param token JWT token
     * @param projectId The ID of the project to be retrieved.
     * @return The details of the project.
     */
    fun getProject(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String
    ) : ResponseEntity<Project>

    /**
     * Updates the details of a project.
     *
     * @param token JWT token
     * @param projectId The ID of the project to be updated.
     * @param request The new details of the project similar to CreateProjectRequest,
     *                all fields are optional and only the provided ones will be updated.
     */
    fun updateProject(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String,
        @RequestBody request: UpdateProjectRequest
    ) : ResponseEntity<Project>

    /**
     * Retrieves a list of projects based on the provided filters.
     *
     * @param token JWT token
     * @param query The search query to filter projects by name, description or usernames.
     * @param page The page number for pagination.
     * @param size The number of projects per page.
     * @param status The status of the projects to filter by.
     * @param priority The priority of the projects to filter by.
     * @param sortBy The field to sort the projects by.
     * @param sortDirection The direction to sort the projects (ascending or descending).
     */
    fun listProjects(
        @JwtToken token: JwtTokenData?,
        @Param("query") query: String?,
        @Param("page") page: Int,
        @Param("size") size: Int,
        @Param("status") status: ProjectStatus?,
        @Param("priority") priority: ProjectPriority?,
        @Param("sortBy") sortBy: String?,
        @Param("sortDirection") sortDirection: SortDirection?,
    ) : ResponseEntity<List<Project>>

    /**
     * Deletes a project by its ID.
     *
     * @param token JWT token
     * @param projectId The ID of the project to be deleted.
     * @return A boolean indicating whether the deletion was successful.
     */
    fun deleteProject(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String
    ): ResponseEntity<Unit>

    /**
     * Creates a new role for a project.
     *
     * @param token JWT token
     * @param projectId The ID of the project to create the role for.
     * @param roleName The name of the role to be created.
     */
    fun createProjectRole(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String,
        @RequestBody roleName: String
    ): ResponseEntity<Unit>

    /**
     * Deletes a role from a project. Deleting a role when there are users assigned to it are not allowed.
     *
     * @param token JWT token
     * @param projectId The ID of the project to delete the role from.
     * @param roleName The name of the role to be deleted.
     */
    fun deleteProjectRole(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String,
        @RequestBody roleName: String
    ): ResponseEntity<Unit>

    /**
     * Assigns a users to a project with a specific role.
     *
     * @param token JWT token
     * @param projectId The ID of the project to assign the user to.
     * @param assignUsersToProjectRequest The request containing the user IDs and role name.
     */
    fun assignUsersToProject(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String,
        @RequestBody assignUsersToProjectRequest: AssignUserToProjectRequest
    ) : ResponseEntity<Unit>

    /**
     * Removes users from a project.
     *
     * @param token JWT token
     * @param projectId The ID of the project to remove the user from.
     * @param userIds The list of user IDs to be removed from the project.
     */
    fun removeUserFromProject(
        @JwtToken token: JwtTokenData?,
        @PathVariable("projectId") projectId: String,
        @RequestBody userIds: List<Long>
    ) : ResponseEntity<Unit>
}