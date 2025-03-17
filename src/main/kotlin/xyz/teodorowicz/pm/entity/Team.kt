package xyz.teodorowicz.pm.entity

import jakarta.persistence.*

@Entity
@Table(name = "teams")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    
    val name: String,
    
    val description: String? = null,
    
    @ManyToOne
    @JoinColumn(name = "ownerId")
    val owner: User,
    
    @OneToMany(mappedBy = "team")
    val tasks: MutableList<Task> = mutableListOf(),
    
    @OneToMany(mappedBy = "team")
    val files: MutableList<File> = mutableListOf(),
    
    @OneToMany(mappedBy = "team")
    val messages: MutableList<Message> = mutableListOf(),
    
    @OneToMany(mappedBy = "team")
    val notifications: MutableList<Notification> = mutableListOf(),
    
    @ManyToMany(mappedBy = "teams")
    val users: MutableList<User> = mutableListOf()
)
