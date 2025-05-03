package xyz.teodorowicz.pm.service

import xyz.teodorowicz.pm.dto.request.project.AssignUserToProjectRequest
import xyz.teodorowicz.pm.dto.request.project.CreateProjectRequest
import xyz.teodorowicz.pm.dto.request.project.UpdateProjectRequest
import xyz.teodorowicz.pm.entity.Project
import xyz.teodorowicz.pm.enumeration.SortDirection
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.model.JwtTokenData

interface ProjectService {
    fun createProject(token: JwtTokenData?, request: CreateProjectRequest): Project
    
    fun getProject(projectId: String): Project
    
    fun updateProject(projectId: String, request: UpdateProjectRequest): Project
    
    fun listProjects(
        s: String?,
        page: Int = 0,
        size: Int = 20,
        status: ProjectStatus?,
        priority: ProjectPriority?,
        sortBy: String?,
        sortDirection: SortDirection?
    ): List<Project>
    
    fun deleteProject(token: JwtTokenData?, projectId: String)
    
    fun createProjectRole(token: JwtTokenData?, projectId: String, roleName: String)
    
    fun deleteProjectRole(token: JwtTokenData?, projectId: String, roleName: String)
    
    fun assignUsersToProject(token: JwtTokenData?, projectId: String, request: AssignUserToProjectRequest)
    
    fun removeUserFromProject(token: JwtTokenData?, projectId: String, userIds: List<Long>)
}
