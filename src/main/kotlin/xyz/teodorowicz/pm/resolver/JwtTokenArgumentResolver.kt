package xyz.teodorowicz.pm.resolver

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import xyz.teodorowicz.pm.annotation.JwtToken
import xyz.teodorowicz.pm.model.JwtTokenData
import xyz.teodorowicz.pm.service.SecurityService
import jakarta.servlet.http.HttpServletRequest
import xyz.teodorowicz.pm.model.JwtTokenClaims
import xyz.teodorowicz.pm.exception.UnauthorizedException

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
            ?: throw UnauthorizedException("Authorization header is missing")

        if (!authHeader.startsWith("Bearer ")) {
            throw UnauthorizedException("Invalid token format")
        }

        val token = authHeader.substring(7)

        try {
            if (!securityService.verifyToken(token)) {
                throw UnauthorizedException("Invalid token")
            }
            val claims = securityService.getTokenClaims(token)
            val parsedClaims = JwtTokenClaims.fromMap(claims)
            return JwtTokenData(claims = parsedClaims, token = token)
        } catch (e: ExpiredJwtException) {
            throw UnauthorizedException("Token has expired")
        } catch (e: SignatureException) {
            throw UnauthorizedException("Invalid token signature")
        } catch (e: MalformedJwtException) {
            throw UnauthorizedException("Invalid token format")
        } catch (e: Exception) {
            throw UnauthorizedException("Invalid token")
        }
    }
}
