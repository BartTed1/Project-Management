package xyz.teodorowicz.pm.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "file")
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    
    val name: String,
    
    val path: String,
    
    @ManyToOne
    @JoinColumn(name = "userId")
    val user: User,
    
    @ManyToOne
    @JoinColumn(name = "teamId")
    val team: Team,
    
    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
