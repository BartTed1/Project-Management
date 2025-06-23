package xyz.teodorowicz.pm.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.dto.request.task.CreateTaskRequest
import xyz.teodorowicz.pm.dto.request.team.CreateTeamRequest
import xyz.teodorowicz.pm.entity.Task
import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.model.JwtTokenData

interface TaskController {

    /**
     * Get all Tasks
     * @param token JWT token
     * @param pageable Pagination information
     * @return ResponseEntity with a page of teams
     */
    fun getTasks(
        @JwtToken token: JwtTokenData?,
        pageable: Pageable
    ): ResponseEntity<Page<Task>>


    /**
     * Retrieves the details of a Task by its ID.
     *
     * @param token JWT token
     * @param taskId The ID of the team to be retrieved.
     * @return The details of the team.
     */
    fun getTask(
        @JwtToken token: JwtTokenData?,
        @PathVariable("taskId") taskId: String
    ) : ResponseEntity<Task>

    /**
     * Creates a new task with the given details.
     *
     * @param token JWT token
     * @param body The details of the task to be created.
     * @return The created task.
     */
    fun createTask(
        @JwtToken token: JwtTokenData?,
        @RequestBody body: CreateTaskRequest
    ): ResponseEntity<Task>

    /**
     * Updates the details of a task.
     *
     * @param token JWT token
     * @param taskId The ID of the task to be updated.
     * @param request The new details of the team similar to CreateTaskRequest,
     *                all fields are optional and only the provided ones will be updated.
     */
    fun updateTask(
        @JwtToken token: JwtTokenData,
        @PathVariable("taskId") taskId: String,
        @RequestBody request: CreateTaskRequest
    ) : ResponseEntity<Task>

    /**
     * Deletes a task by its ID.
     *
     * @param token JWT token
     * @param taskId The ID of the task to be deleted.
     * @return A boolean indicating whether the deletion was successful.
     */
    fun deleteTask(
        @JwtToken token: JwtTokenData,
        @PathVariable("taskId") taskId: String
    ): ResponseEntity<Unit>

    /**
     * update task status
     *
     * @param token JWT token
     * @param taskId The ID of the task to be update status.
     * @param status status DURING, COMPLETED, UNCOMPLETED
     * @return The updated task.
     */
    fun updateTaskStatus(
        @JwtToken token: JwtTokenData,
        @PathVariable("taskId") taskId: String,
        @PathVariable("status") status: String
    ): ResponseEntity<Task>
}