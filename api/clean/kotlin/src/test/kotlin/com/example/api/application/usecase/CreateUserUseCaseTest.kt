package com.example.api.application.usecase

import com.example.api.domain.model.User
import com.example.api.domain.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class CreateUserUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val createUserUseCase = CreateUserUseCase(userRepository)

    @Test
    fun `should create user when email is unique`() {
        val email = "test@example.com"
        val name = "Test User"
        val savedUser = User(1L, email, name, LocalDateTime.now(), LocalDateTime.now())

        every { userRepository.findByEmail(email) } returns null
        every { userRepository.save(any()) } returns savedUser

        val result = createUserUseCase.execute(email, name)

        assertEquals(savedUser, result)
        verify { userRepository.findByEmail(email) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should throw exception when email already exists`() {
        val email = "test@example.com"
        val name = "Test User"
        val existingUser = User(1L, email, "Existing User", LocalDateTime.now(), LocalDateTime.now())

        every { userRepository.findByEmail(email) } returns existingUser

        val exception = assertThrows<IllegalArgumentException> {
            createUserUseCase.execute(email, name)
        }

        assertEquals("User with email $email already exists", exception.message)
        verify { userRepository.findByEmail(email) }
        verify(exactly = 0) { userRepository.save(any()) }
    }
}