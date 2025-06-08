package com.example.api.application.service

import com.example.api.application.usecase.CreateUserUseCase
import com.example.api.application.usecase.GetAllUsersUseCase
import com.example.api.application.usecase.GetUserUseCase
import com.example.api.domain.model.User
import org.springframework.stereotype.Service

@Service
class UserService(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) {
    fun createUser(email: String, name: String): User {
        return createUserUseCase.execute(email, name)
    }
    
    fun getUser(id: Long): User {
        return getUserUseCase.execute(id)
    }
    
    fun getAllUsers(): List<User> {
        return getAllUsersUseCase.execute()
    }
}