package xyz.teodorowicz.pm.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import xyz.teodorowicz.pm.entity.Notification

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    
    fun findByUserIdAndReadFalseOrderByCreatedAtDesc(userId: Long): List<Notification>
    
    fun findByUserIdOrderByCreatedAtDesc(userId: Long, pageable: Pageable): Page<Notification>
    
    fun countByUserIdAndReadFalse(userId: Long): Long
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :notificationId AND n.user.id = :userId")
    fun markAsReadByIdAndUserId(@Param("notificationId") notificationId: Long, @Param("userId") userId: Long): Int
    
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user.id = :userId AND n.read = false")
    fun markAllAsReadByUserId(@Param("userId") userId: Long): Int
}
