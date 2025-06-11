package xyz.teodorowicz.pm.controller

import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.dto.response.NotificationCountResponse
import xyz.teodorowicz.pm.dto.response.NotificationResponse
import xyz.teodorowicz.pm.dto.response.Response
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.service.NotificationService

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService
) {

    @GetMapping
    fun getAllNotifications(
        @JwtToken tokenData: JwtTokenData,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<Page<NotificationResponse>> {
        val notifications = notificationService.getAllNotifications(tokenData.claims.userId, page, size)
        return ResponseEntity.ok(notifications)
    }

    @GetMapping("/unread")
    fun getUnreadNotifications(
        @JwtToken tokenData: JwtTokenData
    ): ResponseEntity<List<NotificationResponse>> {
        val notifications = notificationService.getUnreadNotifications(tokenData.claims.userId)
        return ResponseEntity.ok(notifications)
    }

    @GetMapping("/count")
    fun getUnreadCount(
        @JwtToken tokenData: JwtTokenData
    ): ResponseEntity<NotificationCountResponse> {
        val count = notificationService.getUnreadCount(tokenData.claims.userId)
        return ResponseEntity.ok(count)
    }

    @PutMapping("/{id}/read")
    fun markAsRead(
        @PathVariable id: Long,
        @JwtToken tokenData: JwtTokenData
    ): ResponseEntity<Response> {
        val success = notificationService.markAsRead(id, tokenData.claims.userId)
        return if (success) {
            ResponseEntity.ok(Response("Notification marked as read"))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/read-all")
    fun markAllAsRead(
        @JwtToken tokenData: JwtTokenData
    ): ResponseEntity<Response> {
        val success = notificationService.markAllAsRead(tokenData.claims.userId)
        return ResponseEntity.ok(Response("All notifications marked as read"))
    }
}
