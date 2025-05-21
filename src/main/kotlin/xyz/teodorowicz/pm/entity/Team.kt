package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*

@Entity
@Table(name = "team")
data class Team (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String = "",

    @Column
    var description: String = "",

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnoreProperties(value = ["ownedTeams", "teams", "password"])
    var owner: User,

    @ManyToMany
    @JoinTable(
        name = "user_team",
        joinColumns = [JoinColumn(name = "team_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    @JsonIgnoreProperties(value = ["ownedTeams", "teams", "password"])
    var users: MutableList<User> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    @JsonBackReference("team")
    var tasks: MutableList<Task> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    @JsonBackReference("team")
    var files: MutableList<File> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    @JsonBackReference("team")
    var messages: MutableList<Message> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    @JsonBackReference("team")
    var notifications: MutableList<Notification> = mutableListOf()
)