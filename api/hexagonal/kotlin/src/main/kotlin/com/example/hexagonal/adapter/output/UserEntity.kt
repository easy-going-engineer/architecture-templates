package com.example.hexagonal.adapter.output

import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.model.UserId
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    val id: String,
    
    @Column(nullable = false)
    val name: String,
    
    @Column(nullable = false, unique = true)
    val email: String,
    
    @Column(nullable = false)
    val age: Int
) {
    fun toDomain(): User {
        return User(
            id = UserId.of(id),
            name = name,
            email = email,
            age = age
        )
    }

    companion object {
        fun fromDomain(user: User): UserEntity {
            return UserEntity(
                id = user.id.value,
                name = user.name,
                email = user.email,
                age = user.age
            )
        }
    }
}