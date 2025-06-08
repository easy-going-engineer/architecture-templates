package com.example.hexagonal.application.usecase

import com.example.hexagonal.domain.port.UserRepository
import org.springframework.stereotype.Service

@Service
class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    fun execute(): GetAllUsersResponse {
        val users = userRepository.findAll()
        
        return GetAllUsersResponse(
            users = users.map { user ->
                UserResponse(
                    id = user.id.value,
                    name = user.name,
                    email = user.email,
                    age = user.age
                )
            }
        )
    }

    data class GetAllUsersResponse(
        val users: List<UserResponse>
    )

    data class UserResponse(
        val id: String,
        val name: String,
        val email: String,
        val age: Int
    )
}