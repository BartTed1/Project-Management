package xyz.teodorowicz.pm.dto.request.project

import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import java.time.LocalDate

data class UpdateProjectRequest(
    val name: String? = null,
    val teamId: Long? = null,
    val description: String? = null,
    val plannedStartDate: LocalDate? = null,
    val plannedEndDate: LocalDate? = null,
    val status: ProjectStatus? = null,
    val priority: ProjectPriority? = null,
)
