package com.kream.root.Detail.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;

public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers(
//                                        "/v2/api-docs",
//                                        "/v3/api-docs",
//                                        "/v3/api-docs/**",
//                                        "/swagger-resources/**",
//                                        "/swagger-ui.html",
//                                        "/swagger-ui/**",
//                                        "/webjars/**",
//                                        "/configuration/ui",
//                                        "/configuration/security"
//                                ).permitAll() // Swagger 관련 URL 허용
//                                .anyRequest().authenticated()
//                )
//                .sessionManagement(sessionManagement ->
//                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                );
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(
//                "/v2/api-docs",
//                "/v3/api-docs",
//                "/v3/api-docs/**",
//                "/swagger-resources/**",
//                "/swagger-ui.html",
//                "/swagger-ui/**",
//                "/webjars/**",
//                "/configuration/ui",
//                "/configuration/security"
//        );
//    }
}
