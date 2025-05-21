package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import xyz.teodorowicz.pm.enumeration.project.ProjectPriority
import xyz.teodorowicz.pm.enumeration.project.ProjectStatus
import xyz.teodorowicz.pm.model.UserWithRole
import java.time.LocalDate

@Entity
@Table(name = "project")
data class Project(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "owner_id")
    var owner: User,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var description: String = "",

    @Column(name = "planned_start_date", nullable = false)
    var plannedStartDate: LocalDate = LocalDate.now(),

    @Column(name = "planned_end_date", nullable = true)
    var plannedEndDate: LocalDate? = null,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_roles", joinColumns = [JoinColumn(name = "project_id")])
    @Column(name = "role")
    var roles: Set<String> = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    var status: ProjectStatus = ProjectStatus.NOT_STARTED,

    @Enumerated(EnumType.STRING)
    var priority: ProjectPriority = ProjectPriority.NORMAL,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "team_users",
        joinColumns = [JoinColumn(name = "team_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var users: MutableSet<User> = mutableSetOf()

)
