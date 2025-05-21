package xyz.teodorowicz.pm.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import xyz.teodorowicz.pm.resolver.JwtTokenArgumentResolver
import xyz.teodorowicz.pm.resolver.SystemRoleInterceptor
import xyz.teodorowicz.pm.service.SecurityService

@Configuration
class WebMvcConfig(
    private val securityService: SecurityService
) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(JwtTokenArgumentResolver(securityService))
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SystemRoleInterceptor(securityService))
    }
}
