package com.example.api.application.usecase

import com.example.api.domain.model.User
import com.example.api.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(id: Long): User {
        return userRepository.findById(id)
            ?: throw IllegalArgumentException("User with id $id not found")
    }
}