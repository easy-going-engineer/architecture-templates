package com.example.api.application.usecase

import com.example.api.domain.model.User
import com.example.api.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(email: String, name: String): User {
        if (userRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("User with email $email already exists")
        }
        
        val user = User(
            email = email,
            name = name
        )
        
        return userRepository.save(user)
    }
}