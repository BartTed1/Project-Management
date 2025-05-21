package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.*
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

    @OneToMany(mappedBy = "owner")
    @JsonIgnoreProperties(value = ["owner", "users"])
    var ownedTeams: MutableList<Team> = mutableListOf(),

    @ManyToMany(mappedBy = "users")
    @JsonIgnoreProperties(value = ["users"])
    var teams: MutableList<Team> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @JsonBackReference("user")
    var tasks: MutableList<Task> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @JsonBackReference("user")
    var files: MutableList<File> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @JsonBackReference("user")
    var messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "user")
    @JsonBackReference("user")
    var notifications: MutableList<Notification> = mutableListOf()
)
