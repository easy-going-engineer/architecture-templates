package com.example.hexagonal.application.usecase

import com.example.hexagonal.domain.model.UserId
import com.example.hexagonal.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(request: GetUserRequest): GetUserResponse {
        val userId = UserId.of(request.id)
        val user = userRepository.findById(userId)
            ?: throw NoSuchElementException("User not found with id: ${request.id}")
        
        return GetUserResponse(
            id = user.id.value,
            name = user.name,
            email = user.email,
            age = user.age
        )
    }

    data class GetUserRequest(
        val id: String
    )

    data class GetUserResponse(
        val id: String,
        val name: String,
        val email: String,
        val age: Int
    )
}