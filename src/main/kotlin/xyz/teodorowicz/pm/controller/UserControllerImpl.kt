package xyz.teodorowicz.pm.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.annotation.SystemRole
import xyz.teodorowicz.pm.dto.request.auth.UpdateUserRequest
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.exception.InternalServerErrorException
import xyz.teodorowicz.pm.exception.NotFoundException
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.service.UserService
import xyz.teodorowicz.pm.enumeration.user.SystemRole as SystemRoleEnum

@RestController
@RequestMapping("user")
@Tag(name = "User API", description = "API for user management")
@CrossOrigin(origins = ["*"])
class UserControllerImpl(
    private val userService: UserService
) : UserController {


    @GetMapping
    @Operation(summary = "Get users", description = "Retrieves a list of users")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
        ]
    )
    override fun getUsers(
        @Parameter(description = "JWT token")
        @JwtToken
        token: JwtTokenData?,

        @Parameter(description = "Page number")
        @RequestParam
        page: Int,

        @Parameter(description = "Page size")
        @RequestParam
        size: Int
    ): ResponseEntity<List<User>> {
        try {
            val users = userService.getUsers(page, size)
            return ResponseEntity.ok().body(users)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    override fun getUser(
        @Parameter(description = "JWT token")
        @JwtToken
        token: JwtTokenData?,

        @Parameter(description = "User ID")
        @PathVariable
        id: Long
    ): ResponseEntity<User> {
        try {
            val user = userService.getUser(id) ?: throw NotFoundException()
            return ResponseEntity.ok().body(user)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    @GetMapping("/email")
    @Operation(summary = "Get user by email", description = "Retrieves a user by their email")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "User not found"),
        ]
    )
    override fun getUserByEmail(
        @Parameter(description = "JWT token")
        @JwtToken
        token: JwtTokenData?,

        @Parameter(description = "Email of the user to retrieve")
        @RequestParam
        email: String
    ): ResponseEntity<User> {
        try {
            val user = userService.getUserByEmail(email) ?: throw NotFoundException()
            return ResponseEntity.ok().body(user)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User updated successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "User not found"),
        ]
    )
    override fun updateUser(
        @Parameter(description = "JWT token")
        @JwtToken
        token: JwtTokenData?,

        @Parameter(description = "User ID")
        @PathVariable
        id: Long,

        @Parameter(description = "User data to update")
        @RequestBody
        body: UpdateUserRequest
    ): ResponseEntity<User> {
        try {
            val updatedUser = userService.updateUser(id, body)
            return ResponseEntity.ok().body(updatedUser)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    @SystemRole(SystemRoleEnum.ADMIN)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "User deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "403", description = "Forbidden"),
            ApiResponse(responseCode = "404", description = "User not found"),
        ]
    )
    override fun deleteUser(
        @Parameter(description = "JWT token")
        @JwtToken
        token: JwtTokenData?,

        @Parameter(description = "ID of the user to delete")
        @PathVariable("id")
        id: Long
    ): ResponseEntity<Unit> {
        try {

            userService.deleteUser(id)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }
}