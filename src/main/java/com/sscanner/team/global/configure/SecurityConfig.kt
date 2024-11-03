package com.sscanner.team.global.configure

import com.sscanner.team.auth.jwt.LoginFilter
import com.sscanner.team.auth.jwt.CustomLogoutFilter
import com.sscanner.team.auth.jwt.JWTFilter
import com.sscanner.team.auth.jwt.JWTUtil
import com.sscanner.team.auth.repository.RedisRefreshTokenRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil,
    private val refreshRepository: RedisRefreshTokenRepository
) {

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers("/login", "/", "health", "api/users/join", "/sms/**", "/api/users/reset-password", "/api/users/find-id", "/reissue")
                    .permitAll()
                    .requestMatchers("/api/admin/boards/**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
            }

        configureFilters(http)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOriginPatterns = listOf("http://localhost:*", "http://10.0.2.2:*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    private fun configureFilters(http: HttpSecurity) {
        val jwtFilter = JWTFilter(jwtUtil)
        val loginFilter = LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository)
        val logoutFilter = CustomLogoutFilter(jwtUtil, refreshRepository)

        http.addFilterAfter(jwtFilter, LoginFilter::class.java)
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(logoutFilter, LogoutFilter::class.java)
    }
}
