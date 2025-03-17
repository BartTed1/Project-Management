package xyz.teodorowicz.pm.entity

import jakarta.persistence.*

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    
    val name: String,
    
    @Column(unique = true)
    val email: String,
    
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
