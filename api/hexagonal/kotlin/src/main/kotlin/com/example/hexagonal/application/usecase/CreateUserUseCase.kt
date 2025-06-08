package com.example.hexagonal.application.usecase

import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(request: CreateUserRequest): CreateUserResponse {
        val user = User.create(
            name = request.name,
            email = request.email,
            age = request.age
        )
        
        val savedUser = userRepository.save(user)
        
        return CreateUserResponse(
            id = savedUser.id.value,
            name = savedUser.name,
            email = savedUser.email,
            age = savedUser.age
        )
    }

    data class CreateUserRequest(
        val name: String,
        val email: String,
        val age: Int
    )

    data class CreateUserResponse(
        val id: String,
        val name: String,
        val email: String,
        val age: Int
    )
}