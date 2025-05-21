package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "file")
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    val name: String,
    
    val path: String,

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @ManyToOne
    @JsonBackReference("files")
    @JoinColumn(name = "userId")
    val user: User,
    
    @ManyToOne
    @JsonBackReference("files")
    @JoinColumn(name = "teamId")
    val team: Team
)
