package xyz.teodorowicz.pm.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import xyz.teodorowicz.pm.entity.Task

interface TaskRepository : JpaRepository<Task, String>, JpaSpecificationExecutor<Task>{
    fun findAllByTeamId(teamId: Long, pageable: Pageable): Page<Task>
}