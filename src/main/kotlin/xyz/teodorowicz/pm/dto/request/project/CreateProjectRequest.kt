package xyz.teodorowicz.pm.dto.request.project

import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import java.time.LocalDate

data class CreateProjectRequest(
    val name: String,
    val description: String? = "",
    val plannedStartDate: LocalDate? = null,
    val plannedEndDate: LocalDate? = null,
    val status: ProjectStatus? = ProjectStatus.NOT_STARTED,
    val priority: ProjectPriority? = ProjectPriority.NORMAL,
)
