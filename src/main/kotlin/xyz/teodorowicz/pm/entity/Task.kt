package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    var title: String,
    
    var description: String? = null,
    
    @ManyToOne
    @JoinColumn(name = "projectId")
    val project: Project,
    
    @ManyToOne
    @JoinColumn(name = "userId")
    var user: User? = null,
    
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    
    var deadline: LocalDateTime,
    
    var endAt: LocalDateTime? = null,
    
    val status: String = "DURING",
    
    val priority: String = "MEDIUM",
    
    val reminder: String = "NONE"
)
