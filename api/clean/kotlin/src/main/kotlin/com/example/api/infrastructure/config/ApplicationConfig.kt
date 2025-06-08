package com.example.api.infrastructure.config

import com.example.api.domain.repository.UserRepository
import com.example.api.infrastructure.repository.UserRepositoryImpl
import com.example.api.infrastructure.persistence.UserJpaRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {
    
    @Bean
    fun userRepository(userJpaRepository: UserJpaRepository): UserRepository {
        return UserRepositoryImpl(userJpaRepository)
    }
}