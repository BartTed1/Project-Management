package xyz.teodorowicz.pm.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.dto.request.team.CreateTeamRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.model.JwtTokenData

interface TeamController {
    /**
     * Creates a new team with the given details.
     *
     * @param token JWT token
     * @param body The details of the team to be created.
     * @return The created team.
     */
    fun createTeam(
        @JwtToken token: JwtTokenData?,
        @RequestBody body: CreateTeamRequest
    ): ResponseEntity<Team>

    /**
     * Retrieves the details of a team by its ID.
     *
     * @param token JWT token
     * @param teamId The ID of the team to be retrieved.
     * @return The details of the team.
     */
    fun getTeam(
        @JwtToken token: JwtTokenData?,
        @PathVariable("teamId") teamId: String
    ) : ResponseEntity<Team>

    /**
     * Updates the details of a team.
     *
     * @param token JWT token
     * @param teamId The ID of the team to be updated.
     * @param request The new details of the team similar to CreateProjectRequest,
     *                all fields are optional and only the provided ones will be updated.
     */
    fun updateTeam(
        @JwtToken token: JwtTokenData,
        @PathVariable("teamId") teamId: String,
        @RequestBody request: CreateTeamRequest
    ) : ResponseEntity<Team>

    /**
     * Get all teams
     * @param token JWT token
     * @param pageable Pagination information
     * @return ResponseEntity with a page of teams
     */
    fun getTeams(
        @JwtToken token: JwtTokenData?,
        pageable: Pageable
    ): ResponseEntity<Page<Team>>

    /**
     * Deletes a team by its ID.
     *
     * @param token JWT token
     * @param teamId The ID of the team to be deleted.
     * @return A boolean indicating whether the deletion was successful.
     */
    fun deleteTeam(
        @JwtToken token: JwtTokenData,
        @PathVariable("teamId") teamId: String
    ): ResponseEntity<Unit>

    /**
     * Assigns a users to a team.
     *
     * @param token JWT token
     * @param teamid The ID of the team to assign the user to.
     * @param userEmail user Email .
     */
    fun assignUsersToTeam(
        @JwtToken token: JwtTokenData,
        @PathVariable("teamId") teamId: String,
        @RequestBody userEmail: String
    ) : ResponseEntity<Unit>

    /**
     * Removes users from a team.
     *
     * @param token JWT token
     * @param teamID The ID of the team to remove the user from.
     * @param userId The list of user IDs to be removed from the project.
     */
    fun removeUserFromTeamt(
        @JwtToken token: JwtTokenData,
        @PathVariable("projectId") teamId: String,
        @RequestBody userIds: Long
    ) : ResponseEntity<Unit>
}