package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

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
    
    val role: String = "USER",
    
    @OneToMany(mappedBy = "owner")
    val ownedTeams: MutableList<Team> = mutableListOf(),
    
    @OneToMany(mappedBy = "user")
    val tasks: MutableList<Task> = mutableListOf(),
    
    @OneToMany(mappedBy = "user")
    val files: MutableList<File> = mutableListOf(),
    
    @OneToMany(mappedBy = "user")
    val messages: MutableList<Message> = mutableListOf(),
    
    @OneToMany(mappedBy = "user")
    val notifications: MutableList<Notification> = mutableListOf(),
    
    @ManyToMany
    @JoinTable(
        name = "UserTeams",
        joinColumns = [JoinColumn(name = "userId")],
        inverseJoinColumns = [JoinColumn(name = "teamId")]
    )
    val teams: MutableList<Team> = mutableListOf()
)
