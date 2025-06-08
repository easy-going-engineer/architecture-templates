package com.example.hexagonal.domain

import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.model.UserId
import com.example.hexagonal.domain.port.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserRepositoryTest {

    private val userRepository = mockk<UserRepository>()

    @Test
    fun `should save user`() {
        val user = User.create(
            name = "John Doe",
            email = "john.doe@example.com",
            age = 30
        )

        every { userRepository.save(user) } returns user

        val savedUser = userRepository.save(user)

        assertEquals(user, savedUser)
        verify { userRepository.save(user) }
    }

    @Test
    fun `should find user by id`() {
        val user = User.create(
            name = "John Doe",
            email = "john.doe@example.com",
            age = 30
        )

        every { userRepository.findById(user.id) } returns user

        val foundUser = userRepository.findById(user.id)

        assertEquals(user, foundUser)
        verify { userRepository.findById(user.id) }
    }

    @Test
    fun `should return null when user not found`() {
        val userId = UserId.generate()

        every { userRepository.findById(userId) } returns null

        val foundUser = userRepository.findById(userId)

        assertNull(foundUser)
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should find all users`() {
        val users = listOf(
            User.create("John Doe", "john@example.com", 30),
            User.create("Jane Doe", "jane@example.com", 25)
        )

        every { userRepository.findAll() } returns users

        val foundUsers = userRepository.findAll()

        assertEquals(2, foundUsers.size)
        assertEquals(users, foundUsers)
        verify { userRepository.findAll() }
    }

    @Test
    fun `should delete user by id`() {
        val userId = UserId.generate()

        every { userRepository.deleteById(userId) } returns true

        val result = userRepository.deleteById(userId)

        assertEquals(true, result)
        verify { userRepository.deleteById(userId) }
    }
}