package xyz.teodorowicz.pm.service

import org.springframework.data.domain.Page
import xyz.teodorowicz.pm.dto.response.NotificationCountResponse
import xyz.teodorowicz.pm.dto.response.NotificationResponse
import xyz.teodorowicz.pm.entity.Notification

interface NotificationService {
    
    fun createNotification(userId: Long, teamId: Long, title: String, content: String): Notification
    
    fun getUnreadNotifications(userId: Long): List<NotificationResponse>
    
    fun getAllNotifications(userId: Long, page: Int, size: Int): Page<NotificationResponse>
    
    fun markAsRead(notificationId: Long, userId: Long): Boolean
    
    fun markAllAsRead(userId: Long): Boolean
    
    fun getUnreadCount(userId: Long): NotificationCountResponse
    
    fun sendRealTimeNotification(userId: Long, notification: NotificationResponse)
}
