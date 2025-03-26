package xyz.teodorowicz.pm.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOriginPattern("*") // Pozwala na dostęp z dowolnego źródła
        config.addAllowedHeader("*") // Pozwala na dowolne nagłówki
        config.addAllowedMethod("*") // Pozwala na dowolne metody (GET, POST, PUT, DELETE, itd.)

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}