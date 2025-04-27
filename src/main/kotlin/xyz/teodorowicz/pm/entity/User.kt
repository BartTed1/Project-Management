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

    val name: String,

    @Column(unique = true)
    val email: String,

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    val password: String,

    @Column(nullable = false)
    val role: SystemRole? = SystemRole.USER,

    @OneToMany(mappedBy = "owner")
    val ownedTeams: MutableList<Project> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    val tasks: MutableList<Task> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    val files: MutableList<File> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    val messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    val notifications: MutableList<Notification> = mutableListOf(),

    @ManyToMany
    val projects: MutableList<Project> = mutableListOf(),
)
