package xyz.teodorowicz.pm.repository

import org.springframework.data.jpa.repository.JpaRepository
import xyz.teodorowicz.pm.entity.Message

interface MessageRepository : JpaRepository<Message, Long> {
    fun findAllByTeamIdOrderByCreatedAtAsc(teamId: Long): List<Message>
}
