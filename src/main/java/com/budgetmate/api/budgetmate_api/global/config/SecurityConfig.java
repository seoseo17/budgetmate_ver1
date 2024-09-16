package com.budgetmate.api.budgetmate_api.global.config;

import com.budgetmate.api.budgetmate_api.global.security.authorization.SecurityAccessDeniedHandler;
import com.budgetmate.api.budgetmate_api.global.security.authorization.SecurityAuthenticationEntryPoint;
import com.budgetmate.api.budgetmate_api.global.security.filter.JwtFilter;
import com.budgetmate.api.budgetmate_api.global.security.filter.LoginFilter;
import com.budgetmate.api.budgetmate_api.global.security.util.TokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenManager tokenManager;

    private final String[] PERMIT_URL_ARRAY = {
        "/v3/api-docs/**", "/swagger-ui/**", "/v3/api-docs", "/swagger-ui.html",
        "/error", "/user/signup", "/user/login"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorize-> authorize
            .requestMatchers(PERMIT_URL_ARRAY).permitAll()
            .anyRequest().authenticated())
            .csrf(csrf->csrf.disable())
            .formLogin(formLogin -> formLogin.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .sessionManagement(sessionManagement-> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout(logout -> logout
                .logoutUrl("/user/logout"))
            .exceptionHandling(exceptionHandling -> exceptionHandling //예외처리 설정
                .authenticationEntryPoint(authenticationEntryPoint()) //인증되지 않는 사용자가 보호된 리소스 접근 시
                .accessDeniedHandler(accessDeniedHandler()) // 접근 거부 시
            )
            .addFilterBefore(new JwtFilter(tokenManager), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(new LoginFilter(objectMapper, authenticationManager(authenticationConfiguration), tokenManager), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    public AccessDeniedHandler accessDeniedHandler(){
        return new SecurityAccessDeniedHandler();
    }
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new SecurityAuthenticationEntryPoint(objectMapper);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }
}
