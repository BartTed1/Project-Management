package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "message")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val content: String,
    
    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,
    
    @ManyToOne
    @JoinColumn(name = "teamId")
    val team: Team,
    
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
