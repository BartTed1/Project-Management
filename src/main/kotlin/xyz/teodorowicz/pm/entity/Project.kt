package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import java.time.LocalDate

@Entity
@Table(name = "project")
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @ManyToOne
    @JoinColumn(name = "owner_id")
    val owner: User,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val description: String = "",

    @Column(name = "planned_start_date", nullable = false)
    val plannedStartDate: LocalDate = LocalDate.now(),

    @Column(name = "planned_end_date", nullable = true)
    val plannedEndDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    val status: ProjectStatus = ProjectStatus.NOT_STARTED,

    @Enumerated(EnumType.STRING)
    val priority: ProjectPriority = ProjectPriority.NORMAL,

    @ManyToMany(fetch = FetchType.EAGER)
    val teamMembers: MutableList<User> = mutableListOf(),
)
