package com.example.hexagonal.adapter.output

import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.model.UserId
import com.example.hexagonal.domain.port.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository
) : UserRepository {

    override fun save(user: User): User {
        val userEntity = UserEntity.fromDomain(user)
        val savedEntity = userJpaRepository.save(userEntity)
        return savedEntity.toDomain()
    }

    override fun findById(id: UserId): User? {
        return userJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAll(): List<User> {
        return userJpaRepository.findAll()
            .map { it.toDomain() }
    }

    override fun deleteById(id: UserId): Boolean {
        return if (userJpaRepository.existsById(id.value)) {
            userJpaRepository.deleteById(id.value)
            true
        } else {
            false
        }
    }
}