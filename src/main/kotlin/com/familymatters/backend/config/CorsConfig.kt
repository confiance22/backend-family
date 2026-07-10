package com.familymatters.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import java.util.StringTokenizer

@Configuration
class CorsConfig {

    @Value("\${app.cors.allowed-origins:http://localhost:3000,http://10.0.2.2:8080}")
    private lateinit var allowedOrigins: String

    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        val origins = mutableListOf<String>()
        val tokenizer = StringTokenizer(allowedOrigins, ",")
        while (tokenizer.hasMoreTokens()) {
            origins.add(tokenizer.nextToken().trim())
        }
        config.setAllowedOriginPatterns(origins)
        config.allowedMethods = listOf("*")
        config.allowedHeaders = listOf("*")
        config.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
        return CorsFilter(source)
    }
}
