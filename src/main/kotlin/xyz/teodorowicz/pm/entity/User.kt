package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import xyz.teodorowicz.pm.enumeration.user.SystemRole

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    var name: String,

    @Column(unique = true)
    var email: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password: String,

    @Column(nullable = false)
    var role: SystemRole? = SystemRole.USER,

    @OneToMany(
        mappedBy = "owner",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        fetch = FetchType.LAZY
    )
    var ownedProjects: MutableList<Project> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var tasks: MutableList<Task> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var files: MutableList<File> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    var notifications: MutableList<Notification> = mutableListOf(),

    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var projectMemberships: MutableSet<ProjectMember> = mutableSetOf(),
)
