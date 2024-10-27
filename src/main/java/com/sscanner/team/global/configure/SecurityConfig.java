package com.sscanner.team.global.configure;

import com.sscanner.team.auth.jwt.CustomLogoutFilter;
import com.sscanner.team.auth.jwt.JWTFilter;
import com.sscanner.team.auth.jwt.JWTUtil;
import com.sscanner.team.auth.jwt.LoginFilter;
import com.sscanner.team.auth.repository.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RedisRefreshTokenRepository refreshRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf)-> csrf.disable());
        http.formLogin((auth) -> auth.disable());
        http.httpBasic((auth) -> auth.disable());

        // 경로별 인가
        http.authorizeHttpRequests((authorize)->
                authorize.requestMatchers("/login","/", "health","api/users/join","/sms/**","/api/users/reset-password","/api/users/find-id","/reissue").permitAll()
                        .requestMatchers("/api/admin/boards/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
        );

        http.addFilterAfter(new JWTFilter(jwtUtil), LoginFilter.class);
        http.addFilterAt(
                new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil,refreshRepository), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);
        http .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
