package xyz.teodorowicz.pm.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import xyz.teodorowicz.pm.dto.response.NotificationCountResponse
import xyz.teodorowicz.pm.dto.response.NotificationResponse
import xyz.teodorowicz.pm.entity.Notification
import xyz.teodorowicz.pm.entity.Team
import xyz.teodorowicz.pm.entity.User
import xyz.teodorowicz.pm.repository.NotificationRepository
import xyz.teodorowicz.pm.repository.TeamRepository
import xyz.teodorowicz.pm.repository.UserRepository
import xyz.teodorowicz.pm.websocket.WebSocketSessionManager

@Service
@Transactional
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val webSocketSessionManager: WebSocketSessionManager
) : NotificationService {

    override fun createNotification(userId: Long, teamId: Long, title: String, content: String): Notification {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found with id: $userId") }
        
        val team = teamRepository.findById(teamId)
            .orElseThrow { IllegalArgumentException("Team not found with id: $teamId") }

        val notification = Notification(
            title = title,
            content = content,
            user = user,
            team = team
        )

        val savedNotification = notificationRepository.save(notification)
        
        // Send real-time notification
        val notificationResponse = toNotificationResponse(savedNotification)
        sendRealTimeNotification(userId, notificationResponse)
        
        return savedNotification
    }

    @Transactional(readOnly = true)
    override fun getUnreadNotifications(userId: Long): List<NotificationResponse> {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
            .map { toNotificationResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun getAllNotifications(userId: Long, page: Int, size: Int): Page<NotificationResponse> {
        val pageable = PageRequest.of(page, size)
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
            .map { toNotificationResponse(it) }
    }

    override fun markAsRead(notificationId: Long, userId: Long): Boolean {
        val updated = notificationRepository.markAsReadByIdAndUserId(notificationId, userId)
        return updated > 0
    }

    override fun markAllAsRead(userId: Long): Boolean {
        val updated = notificationRepository.markAllAsReadByUserId(userId)
        return updated > 0
    }

    @Transactional(readOnly = true)
    override fun getUnreadCount(userId: Long): NotificationCountResponse {
        val count = notificationRepository.countByUserIdAndReadFalse(userId)
        return NotificationCountResponse(count)
    }

    override fun sendRealTimeNotification(userId: Long, notification: NotificationResponse) {
        webSocketSessionManager.sendNotificationToUser(userId, notification)
    }

    private fun toNotificationResponse(notification: Notification): NotificationResponse {
        return NotificationResponse(
            id = notification.id,
            title = notification.title,
            content = notification.content,
            createdAt = notification.createdAt,
            read = notification.read,
            teamName = notification.team.name
        )
    }
}
