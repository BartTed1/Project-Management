package xyz.teodorowicz.pm.resolver

import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.stereotype.Component
import xyz.teodorowicz.pm.annotation.SystemRole
import xyz.teodorowicz.pm.exception.ForbiddenException
import xyz.teodorowicz.pm.service.SecurityService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class SystemRoleInterceptor(
    private val securityService: SecurityService
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler !is HandlerMethod) {
            return true
        }

        val systemRoleAnnotation = handler.method.getAnnotation(SystemRole::class.java) ?: return true

        val authHeader = request.getHeader("Authorization") ?: return true
        if (!authHeader.startsWith("Bearer ")) {
            return true
        }

        val token = authHeader.substring(7)
        val userRole = securityService.getRoleFromToken(token)

        if (!systemRoleAnnotation.roles.any { it.name == userRole }) {
            throw ForbiddenException("User does not have the required role")
        }

        return true
    }
}
