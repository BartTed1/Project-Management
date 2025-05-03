package xyz.teodorowicz.pm.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import xyz.teodorowicz.pm.repository.ProjectRepository
import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.entity.ProjectMember
import xyz.teodorowicz.pm.enumeration.SortDirection
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.enumeration.user.SystemRole
import xyz.teodorowicz.pm.exception.ForbiddenException
import xyz.teodorowicz.pm.exception.NotFoundException
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.repository.UserRepository
import xyz.teodorowicz.pm.specification.ProjectSpecification
import java.time.LocalDate

@Service
class ProjectServiceImpl(
    private val projectRepository: ProjectRepository,
    private val userRepository: UserRepository
) : ProjectService {
    override fun createProject(token: JwtTokenData?, request: CreateProjectRequest): Project {
        if (token == null) throw IllegalArgumentException("Token cannot be null")

        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        val project = Project(
            name = request.name,
            description = request.description ?: "",
            status = request.status ?: ProjectStatus.NOT_STARTED,
            priority = request.priority ?: ProjectPriority.NORMAL,
            plannedStartDate = request.plannedStartDate ?: LocalDate.now(),
            plannedEndDate = request.plannedEndDate,
            owner = user,
            members = mutableSetOf(),
        )
        return projectRepository.save(project)
    }

    override fun getProject(projectId: String): Project {
        return projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }
    }

    override fun updateProject(projectId: String, request: UpdateProjectRequest): Project {

        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }

        val updatedProject = project.copy(
            name = request.name ?: project.name,
            description = request.description ?: project.description,
            status = request.status ?: project.status,
            priority = request.priority ?: project.priority,
            plannedStartDate = request.plannedStartDate ?: project.plannedStartDate,
            plannedEndDate = request.plannedEndDate ?: project.plannedEndDate,
        )

        return projectRepository.save(updatedProject)
    }

    override fun listProjects(
        s: String?,
        page: Int,
        size: Int,
        status: ProjectStatus?,
        priority: ProjectPriority?,
        sortBy: String?,
        sortDirection: SortDirection?
    ): List<Project> {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(
                if (sortDirection == SortDirection.DESC) Sort.Direction.DESC else Sort.Direction.ASC,
                sortBy ?: "id"
            )
        )
        val specification = ProjectSpecification(
            search = s,
            status = status,
            priority = priority
        )
        return projectRepository.findAll(specification, pageable).content
    }

    override fun deleteProject(token: JwtTokenData?, projectId: String) {
        if (token == null) {
            throw IllegalArgumentException("Token cannot be null")
        }

        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }

        val user = userRepository.findById(token.claims.userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        if (project.owner.id == user.id) {
            projectRepository.delete(project)
        } else if (user.role == SystemRole.ADMIN) {
            projectRepository.delete(project)
        } else {
            throw ForbiddenException("You do not have permission to delete this project")
        }
    }

    override fun createProjectRole(token: JwtTokenData?, projectId: String, roleName: String) {
        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }

        val existingRole = project.roles.contains(roleName)
        if (existingRole) {
            throw IllegalArgumentException("Role already exists in the project")
        }

        val updatedProject = project.copy(
            roles = project.roles.plus(roleName),
        )
        projectRepository.save(updatedProject)
    }

    override fun deleteProjectRole(token: JwtTokenData?, projectId: String, roleName: String) {
        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }

        val existingRole = project.roles.contains(roleName)
        if (!existingRole) {
            throw IllegalArgumentException("Role does not exist in the project")
        }

        val isAnyUserHasRole = project.members.any { it.role == roleName }
        if (isAnyUserHasRole) {
            throw IllegalArgumentException("Cannot delete role that is assigned to users. Remove users from this role first.")
        }

        val updatedProject = project.copy(
            roles = project.roles.minus(roleName),
        )
        projectRepository.save(updatedProject)
    }

    override fun assignUsersToProject(token: JwtTokenData?, projectId: String, request: AssignUserToProjectRequest) {
        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }

        val users = userRepository.findAllById(request.userId)

        val newMembers = users.map { user ->
            ProjectMember(
                user = user,
                project = project,
                role = request.role
            )
        }

        val updatedProject = project.copy(
            members = (project.members + newMembers).toMutableSet()
        )

        projectRepository.save(updatedProject)
    }

    override fun removeUserFromProject(token: JwtTokenData?, projectId: String, userIds: List<Long>) {
        val project = projectRepository.findById(projectId)
            .orElseThrow { NotFoundException("Project not found: $projectId") }

        val membersToRemove = project.members.filter { it.user.id in userIds }
        if (membersToRemove.isEmpty()) {
            throw IllegalArgumentException("No users found in the project with the provided IDs")
        }

        val updatedMembers = project.members.filter { it.user.id !in userIds }.toMutableSet()

        val updatedProject = project.copy(
            members = updatedMembers
        )

        projectRepository.save(updatedProject)
    }

}
