package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val title: String,
    
    val content: String,

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),

    val read: Boolean = false,
    
    @ManyToOne
    @JsonBackReference("notifications")
    @JoinColumn(name = "userId")
    val user: User,
    
    @ManyToOne
    @JsonBackReference("notifications")
    @JoinColumn(name = "teamId")
    val team: Team

)
