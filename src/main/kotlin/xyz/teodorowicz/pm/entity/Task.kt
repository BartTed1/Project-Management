package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val title: String,
    
    val description: String? = null,
    
    @ManyToOne
    @JoinColumn(name = "teamId")
    val team: Team,
    
    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User? = null,
    
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    
    val deadline: LocalDateTime,
    
    val endAt: LocalDateTime? = null,
    
    val status: String = "DURING",
    
    val priority: String = "MEDIUM",
    
    val reminder: String = "NONE"
)
