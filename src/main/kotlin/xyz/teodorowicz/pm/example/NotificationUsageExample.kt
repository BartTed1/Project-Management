package xyz.teodorowicz.pm.example

import org.springframework.stereotype.Component
import xyz.teodorowicz.pm.service.NotificationService

/**
 * Example class showing how to use the NotificationService
 * to create and send notifications in your application.
 */
@Component
class NotificationUsageExample(
    private val notificationService: NotificationService
) {

    /**
     * Example: Create a notification when a new task is assigned
     */
    fun notifyTaskAssignment(userId: Long, teamId: Long, taskName: String) {
        notificationService.createNotification(
            userId = userId,
            teamId = teamId,
            title = "New Task Assigned",
            content = "You have been assigned to task: $taskName"
        )
    }

    /**
     * Example: Create a notification when a project is updated
     */
    fun notifyProjectUpdate(userId: Long, teamId: Long, projectName: String) {
        notificationService.createNotification(
            userId = userId,
            teamId = teamId,
            title = "Project Updated",
            content = "Project '$projectName' has been updated with new information"
        )
    }

    /**
     * Example: Create a notification when a user joins a team
     */
    fun notifyTeamJoin(userId: Long, teamId: Long, teamName: String) {
        notificationService.createNotification(
            userId = userId,
            teamId = teamId,
            title = "Welcome to Team",
            content = "You have successfully joined team: $teamName"
        )
    }

    /**
     * Example: Create a notification for deadline reminders
     */
    fun notifyDeadlineReminder(userId: Long, teamId: Long, taskName: String, daysLeft: Int) {
        notificationService.createNotification(
            userId = userId,
            teamId = teamId,
            title = "Deadline Reminder",
            content = "Task '$taskName' is due in $daysLeft days. Please review your progress."
        )
    }
}
