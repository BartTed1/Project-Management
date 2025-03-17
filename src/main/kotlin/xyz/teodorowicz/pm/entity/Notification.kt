package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    
    val content: String,
    
    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,
    
    @ManyToOne
    @JoinColumn(name = "teamId")
    val team: Team,
    
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    val read: Boolean = false
)
