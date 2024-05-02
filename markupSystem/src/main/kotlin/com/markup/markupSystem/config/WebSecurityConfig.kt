package com.markup.markupSystem.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod.*
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun userDetailsService(): InMemoryUserDetailsManager {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val admin = User.withUsername("admin")
            .password(encoder.encode("admin"))
            .roles("ADMIN")
            .build()
        val user = User.withUsername("user")
            .password(encoder.encode("qwerty"))
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(admin, user)
    }

    @Bean
    @Order(1)
    fun apiFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/service/**")
            .authorizeHttpRequests { authorize ->
                authorize
                    .anyRequest().hasRole("ADMIN")
            }
            .httpBasic(withDefaults())
            .csrf{
                csrf -> csrf.disable()
            }
        return http.build()
    }

    @Bean
    fun formLoginFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .anyRequest().authenticated()
            }
            .formLogin(withDefaults())
        return http.build()
    }
}