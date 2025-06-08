package com.example.api.infrastructure.repository

import com.example.api.domain.model.User
import com.example.api.domain.repository.UserRepository
import com.example.api.infrastructure.persistence.UserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {
    
    override fun save(user: User): User {
        return userJpaRepository.save(user)
    }
    
    override fun findById(id: Long): User? {
        return userJpaRepository.findById(id).orElse(null)
    }
    
    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)
    }
    
    override fun findAll(): List<User> {
        return userJpaRepository.findAll()
    }
    
    override fun deleteById(id: Long) {
        userJpaRepository.deleteById(id)
    }
}