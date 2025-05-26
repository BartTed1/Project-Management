package xyz.teodorowicz.pm.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import xyz.teodorowicz.pm.resolver.JwtTokenArgumentResolver
import xyz.teodorowicz.pm.service.SecurityService

@TestConfiguration
class TestWebConfig(private val securityService: SecurityService) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(JwtTokenArgumentResolver(securityService))
    }

    @Bean
    fun jwtTokenArgumentResolver(): JwtTokenArgumentResolver {
        return JwtTokenArgumentResolver(securityService)
    }
}
