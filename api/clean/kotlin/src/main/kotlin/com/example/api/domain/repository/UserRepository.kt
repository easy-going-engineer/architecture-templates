package com.example.api.domain.repository

import com.example.api.domain.model.User

interface UserRepository {
    fun save(user: User): User
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun findAll(): List<User>
    fun deleteById(id: Long)
}