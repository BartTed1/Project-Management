package xyz.teodorowicz.pm.resolver

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.server.ResponseStatusException
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.service.SecurityService
import jakarta.servlet.http.HttpServletRequest

class JwtTokenArgumentResolver(
    private val securityService: SecurityService
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(JwtToken::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): JwtTokenData {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)
        val authHeader = request?.getHeader("Authorization")
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization header is missing")

        if (!authHeader.startsWith("Bearer ")) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token format")
        }

        val token = authHeader.substring(7)

        try {
            if (!securityService.verifyToken(token)) {
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")
            }
            val claims = securityService.getTokenClaims(token)
            return JwtTokenData(claims = claims, token = token)
        } catch (e: ExpiredJwtException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token has expired")
        } catch (e: SignatureException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token signature")
        } catch (e: MalformedJwtException) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token format")
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")
        }
    }
}
