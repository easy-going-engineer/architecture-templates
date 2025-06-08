package com.example.api.application.usecase

import com.example.api.domain.model.User
import com.example.api.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    fun execute(): List<User> {
        return userRepository.findAll()
    }
}