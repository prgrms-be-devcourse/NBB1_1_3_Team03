package com.sscanner.team.global.configure

import com.sscanner.team.auth.jwt.LoginFilter

@org.springframework.context.annotation.Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig {
    private val authenticationConfiguration: AuthenticationConfiguration? = null
    private val jwtUtil: JWTUtil? = null
    private val refreshRepository: RedisRefreshTokenRepository? = null

    @org.springframework.context.annotation.Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @org.springframework.context.annotation.Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.getAuthenticationManager()
    }

    @org.springframework.context.annotation.Bean
    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf(org.springframework.security.config.Customizer<CsrfConfigurer<HttpSecurity?>> { csrf: CsrfConfigurer<HttpSecurity?> -> csrf.disable() })
        http.formLogin(org.springframework.security.config.Customizer<FormLoginConfigurer<HttpSecurity?>> { auth: FormLoginConfigurer<HttpSecurity?> -> auth.disable() })
        http.httpBasic(org.springframework.security.config.Customizer<HttpBasicConfigurer<HttpSecurity?>> { auth: HttpBasicConfigurer<HttpSecurity?> -> auth.disable() })

        http.sessionManagement(org.springframework.security.config.Customizer<SessionManagementConfigurer<HttpSecurity?>> { session: SessionManagementConfigurer<HttpSecurity?> ->
            session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )
        })
        // 경로별 인가
        http.authorizeHttpRequests(org.springframework.security.config.Customizer<AuthorizationManagerRequestMatcherRegistry> { authorize: AuthorizationManagerRequestMatcherRegistry ->
            authorize.requestMatchers(
                "/login",
                "/",
                "health",
                "api/users/join",
                "/sms/**",
                "/api/users/reset-password",
                "/api/users/find-id",
                "/reissue"
            ).permitAll()
                .requestMatchers("/api/admin/boards/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
        }
        )

        configureFilters(http)

        return http.build()
    }

    @org.springframework.context.annotation.Bean
    fun corsConfigurationSource(): org.springframework.web.cors.CorsConfigurationSource {
        val configuration: CorsConfiguration = CorsConfiguration()

        configuration.setAllowedOriginPatterns(mutableListOf("http://localhost:*", "http://10.0.2.2:*"))

        configuration.setAllowedMethods(mutableListOf("GET", "POST", "PUT", "DELETE"))
        configuration.setAllowedHeaders(listOf("*"))
        configuration.setAllowCredentials(true)

        val source = org.springframework.web.cors.UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Throws(java.lang.Exception::class)
    private fun configureFilters(http: HttpSecurity) {
        val jwtFilter: JWTFilter = JWTFilter(jwtUtil)
        val loginFilter: LoginFilter =
            LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository)
        val logoutFilter: CustomLogoutFilter = CustomLogoutFilter(jwtUtil, refreshRepository)

        http.addFilterAfter(jwtFilter, LoginFilter::class.java)
            .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(logoutFilter, LogoutFilter::class.java)
    }
}
