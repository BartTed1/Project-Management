package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "message")
data class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val content: String,

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ManyToOne
    @JsonBackReference("messages")
    @JoinColumn(name = "userId")
    val user: User,
    
    @ManyToOne
    @JsonBackReference("messages")
    @JoinColumn(name = "teamId")
    val team: Team
)
