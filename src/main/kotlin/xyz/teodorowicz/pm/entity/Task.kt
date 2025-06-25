package xyz.teodorowicz.pm.entity

import com.fasterxml.jackson.annotation.*
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import xyz.teodorowicz.pm.dto.response.TaskResponse
import java.time.LocalDateTime

@Entity
@Table(name = "task")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Column(nullable = false)
    var title: String,

    var description: String? = null,

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    @UpdateTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var deadline: LocalDateTime,

    var endAt: LocalDateTime? = null,

    @Column(nullable = false)
    var status: String = "DURING",

    @Column(nullable = false)
    var priority: String = "MEDIUM",

    @Column(nullable = false)
    var reminder: String = "NONE",

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "userId", nullable = false)
    var user: User? = null,

    @ManyToOne
    @JsonBackReference("tasks")
    @JoinColumn(name = "teamId", nullable = false)
    var team: Team? = null

)