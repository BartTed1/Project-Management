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
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.repository.TeamRepository
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.dto.request.team.CreateTeamRequest
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.exception.InternalServerErrorException
import xyz.teodorowicz.pm.repository.UserRepository

@RestController
@RequestMapping("teams")
@Tag(name = "Team API", description = "API for team management")
@CrossOrigin(origins = ["*"])
class TeamControllerImp(
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository
) : TeamController {
    @PostMapping
    @Operation(summary = "Create a new team", description = "Creates a new team with the given details.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "team created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid team details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        ]
    )
    override fun createTeam(
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Team details", description = "Details of the team to be created")
        @RequestBody body: CreateTeamRequest
    ): ResponseEntity<Team> {
        if (token == null) throw IllegalArgumentException("Token cannot be null")

        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val team = Team(
            name = body.name,
            description = body.description,
            owner = user,
            users = mutableListOf(user)
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(teamRepository.save(team))
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "Get team by ID", description = "Retrieves the team with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "team retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "team not found")
        ]
    )
    override fun getTeam(
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "Team ID", description = "ID of the team to be retrieved")
        @PathVariable("teamId")teamId: String
    ): ResponseEntity<Team> {
        val team = teamRepository.findById(teamId)
        if(team.isEmpty) return ResponseEntity.notFound().build()
        else return ResponseEntity.ok(team.get())
    }

    @PutMapping("/{teamId}")
    @Operation(summary = "Update a team", description = "Update a team")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "team updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid project details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project not found")
        ]
    )
    override fun updateTeam(
        @JwtToken token: JwtTokenData,

        @Parameter(name = "Team ID", description = "ID of the team to be updated")
        @PathVariable("teamId")teamId: String,

        @Parameter(name = "team details", description = "Details of the team to be updated")
        @RequestBody
        request: CreateTeamRequest
    ): ResponseEntity<Team> {
        val team = teamRepository.findById(teamId)
        if(team.isEmpty) return ResponseEntity.notFound().build()
        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if(user.role==SystemRole.USER && team.get().owner!=user)
            return ResponseEntity.status(403).build()

        team.get().name = request.name
        team.get().description = request.description

        teamRepository.save(team.get())
        return ResponseEntity.ok(team.get())
    }

    @GetMapping
    @Operation(summary = "Get teams", description = "Retrieves a paginated teams")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "project retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
        ]
    )
    override fun getTeams(
        @JwtToken token: JwtTokenData?,

        @Parameter(description = "Pagination information", example = "page=0&size=10")
        pageable: Pageable
    ): ResponseEntity<Page<Team>> {
        return try {
            val team = teamRepository.findAll(pageable)
            ResponseEntity.ok(team)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    @DeleteMapping("/{teamId}")
    @Operation(summary = "Delete a team", description = "Deletes the team with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "team deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "team not found")
        ]
    )
    override fun deleteTeam(
        @JwtToken token: JwtTokenData,

        @Parameter(name = "Team ID", description = "ID of the team to be updated")
        @PathVariable("teamId")teamId: String
    ): ResponseEntity<Unit> {
        val team = teamRepository.findById(teamId)
        if(team.isEmpty) return ResponseEntity.notFound().build()
        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if(user.role==SystemRole.USER && team.get().owner!=user)
            return ResponseEntity.status(403).build()

        teamRepository.delete(team.get())

        return ResponseEntity.noContent().build()
    }

    @PostMapping("/{teamId}/users")
    @Operation(summary = "Assign users to a team", description = "Assigns users to the specified team with a specific role.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Users assigned to team successfully"),
            ApiResponse(responseCode = "400", description = "Invalid assignment details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "Project or users not found")
        ]
    )
    override fun assignUsersToTeam(
        @JwtToken token: JwtTokenData,

        @Parameter(name = "Team ID", description = "ID of the team to be updated")
        @PathVariable teamId: String,

        @Parameter(name = "user Email", description = "Details of the users to be assigned")
        @RequestBody userEmail: String
    ): ResponseEntity<Unit> {
        val team = teamRepository.findById(teamId)
        if(team.isEmpty) return ResponseEntity.notFound().build()
        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if(user.role==SystemRole.USER && team.get().owner!=user)
            return ResponseEntity.status(403).build()

        val userAdd = userRepository.findByEmail(userEmail)
        println(userEmail)
        if(userAdd == null) return ResponseEntity.notFound().build()

        if(team.get().users.contains(userAdd)) return ResponseEntity.status(403).build()

        team.get().users.add(userAdd)
        teamRepository.save(team.get())

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{teamId}/users")
    @Operation(summary = "Remove users from a team", description = "Removes the specified users from the team.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Users removed from team successfully"),
            ApiResponse(responseCode = "400", description = "Invalid removal details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "team or users not found")
        ]
    )
    override fun removeUserFromTeamt(
        @JwtToken token: JwtTokenData,

        @Parameter(name = "Team ID", description = "ID of the team to be updated")
        @PathVariable teamId: String,

        @Parameter(name = "User ID", description = " user ID to be removed from the project")
        @RequestBody userIds: Long
    ): ResponseEntity<Unit> {
        val team = teamRepository.findById(teamId)
        if(team.isEmpty) return ResponseEntity.notFound().build()
        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if(user.role==SystemRole.USER && team.get().owner!=user)
            return ResponseEntity.status(403).build()

        val userRemove = userRepository.findById(userIds)
            .orElseThrow { IllegalArgumentException("User not found") }

        if(!team.get().users.contains(userRemove)) return ResponseEntity.notFound().build()

        team.get().users.remove(userRemove)

        teamRepository.save(team.get())

        return ResponseEntity.ok().build()
        // TODO naprawić
    }

}