package com.example.hexagonal.domain.port

import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.model.UserId

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UserId): User?
    fun findAll(): List<User>
    fun deleteById(id: UserId): Boolean
}