package com.example.api.presentation.dto

import com.example.api.domain.model.User
import java.time.LocalDateTime

data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                email = user.email,
                name = user.name,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}