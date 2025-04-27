package xyz.teodorowicz.pm.service

import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.enumeration.SortDirection
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus

interface ProjectService {
    fun createProject(authorizationHeader: String?, request: CreateProjectRequest): Project
    
    fun getProject(authorizationHeader: String?, projectId: String): Project
    
    fun updateProject(authorizationHeader: String?, projectId: String, request: UpdateProjectRequest): Project
    
    fun listProjects(
        authorizationHeader: String?,
        query: String?,
        page: Int,
        size: Int,
        status: ProjectStatus?,
        priority: ProjectPriority?,
        sortBy: String?,
        sortDirection: SortDirection?
    ): List<Project>
    
    fun deleteProject(authorizationHeader: String?, projectId: String)
    
    fun createProjectRole(authorizationHeader: String?, projectId: String, roleName: String)
    
    fun deleteProjectRole(authorizationHeader: String?, projectId: String, roleName: String)
    
    fun assignUsersToProject(authorizationHeader: String?, projectId: String, request: AssignUserToProjectRequest)
    
    fun removeUserFromProject(authorizationHeader: String?, projectId: String, userIds: List<Long>)
}
