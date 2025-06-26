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
import xyz.teodorowicz.pm.dto.request.task.CreateTaskRequest
import xyz.teodorowicz.pm.dto.response.TaskResponse
import xyz.teodorowicz.pm.entity.Task
import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.exception.InternalServerErrorException
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.repository.TaskRepository
import xyz.teodorowicz.pm.repository.TeamRepository
import xyz.teodorowicz.pm.repository.UserRepository
import xyz.teodorowicz.pm.mapper.toResponse

@RestController
@RequestMapping("tasks")
@Tag(name = "Task API", description = "API for task management")
@CrossOrigin(origins = ["*"])
class TaskControllerImlp(
    private val taskRepository: TaskRepository,
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository
) : TaskController{

    @GetMapping
    @Operation(summary = "Get tasks", description = "Retrieves a paginated tasks")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "task retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
        ]
    )
    override fun getTasks(
        @JwtToken token: JwtTokenData?,

        @Parameter
        pageable: Pageable
    ): ResponseEntity<Page<Task>> {
        return try {
            val task = taskRepository.findAll(pageable)
            ResponseEntity.ok(task)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

    @GetMapping("/team/{teamId}")
    @Operation(
        summary = "Get tasks by team ID",
        description = "Retrieves paginated tasks for a specific team"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized"),
            ApiResponse(responseCode = "404", description = "Team not found")
        ]
    )
    fun getTasksByTeamId(
        @Parameter(description = "Team ID")
        @PathVariable teamId: Long,

        @Parameter(hidden = true)
        pageable: Pageable,

        @JwtToken token: JwtTokenData?
    ): ResponseEntity<Page<TaskResponse>> {
        return try {
            val taskPage = taskRepository.findAllByTeamId(teamId, pageable)
            val taskResponsePage = taskPage.map { it.toResponse() }
            ResponseEntity.ok(taskResponsePage)
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }



    @GetMapping("/{taskId}")
    @Operation(summary = "Get task by ID", description = "Retrieves the team with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "task retrieved successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "team not found")
        ]
    )
    override fun getTask(
        @JwtToken token: JwtTokenData?,

        @Parameter(name = "task ID", description = "ID of the task to be retrieved")
        @PathVariable taskId: String
    ): ResponseEntity<TaskResponse> {
        val task = taskRepository.findById(taskId)
        return if (task.isEmpty) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(task.get().toResponse())
        }
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task with the given details.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "team created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid team details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
        ]
    )
    override fun createTask(
        @JwtToken token: JwtTokenData?,
        @Parameter(name = "task details", description = "Details of the task to be created")
        @RequestBody body: CreateTaskRequest
    ): ResponseEntity<Task> {
        if (token == null) throw IllegalArgumentException("Token cannot be null")

        val team = teamRepository.findById(body.teamId.toString())
            .orElseThrow { IllegalArgumentException("team not found") }

        val user = userRepository.findById(body.userId)
            .orElseThrow { IllegalArgumentException("user not found") }

        val task = Task(
            title = body.title,
            description = body.description,
            deadline = body.deadline,
            priority = body.priority,
            user = user,
            team = team
        )
        task.team?.let { println(it.id) }

//        TODO("powiadomienie")

        return ResponseEntity.status(HttpStatus.CREATED).body(taskRepository.save(task))

    }

    @PutMapping("/{taskId}")
    @Operation(summary = "Update a task", description = "Update a task")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "task updated successfully"),
            ApiResponse(responseCode = "400", description = "Invalid task details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "task not found")
        ]
    )
    override fun updateTask(
        @JwtToken token: JwtTokenData,
        @Parameter(name = "task ID", description = "ID of the yask to be updated")
        @PathVariable("taskId") taskId: String,
        @Parameter(name = "task details", description = "Details of the tak to be updated")
        @RequestBody request: CreateTaskRequest
    ): ResponseEntity<Task> {
        val task = taskRepository.findById(taskId)
            .orElseThrow { IllegalArgumentException("task not found") }

        task.title = request.title
        task.description = request.description
        task.deadline = request.deadline
        task.priority = request.priority

//        TODO("powiadomienie")

        taskRepository.save(task)
        return ResponseEntity.ok(task)
    }

    @DeleteMapping("/{taskId}")
    @Operation(summary = "Delete a task", description = "Deletes the task with the specified ID.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "task deleted successfully"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "team not found")
        ]
    )
    override fun deleteTask(
        @JwtToken token: JwtTokenData,
        @Parameter(name = "Task ID", description = "ID of the task to be updated")
        @PathVariable("taskId") taskId: String
    ): ResponseEntity<Unit> {
        val task = taskRepository.findById(taskId)
            .orElseThrow { IllegalArgumentException("task not found") }

        taskRepository.delete(task)

        return ResponseEntity.noContent().build()
    }


    @PutMapping("/{taskId}/status")
    @Operation(summary = "Update a task status", description = "Update a task status")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "successfully"),
            ApiResponse(responseCode = "400", description = "Invalid removal details"),
            ApiResponse(responseCode = "401", description = "Unauthorized - invalid token"),
            ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions"),
            ApiResponse(responseCode = "404", description = "not found")
        ]
    )
    override fun updateTaskStatus(
        @JwtToken token: JwtTokenData,
        @Parameter(name = "Task ID", description = "ID of the task to be updated")
        @PathVariable taskId: String,
        @Parameter(name = "status", description = "New status of the task")
        @RequestParam status: String
    ): ResponseEntity<Task> {
        println("====================id zadania: "+taskId)
        val task = taskRepository.findById(taskId)
            .orElseThrow { IllegalArgumentException("task not found") }

        task.status = status

        taskRepository.save(task)

        // TODO: notification logic
        return ResponseEntity.ok(task)
    }


}
