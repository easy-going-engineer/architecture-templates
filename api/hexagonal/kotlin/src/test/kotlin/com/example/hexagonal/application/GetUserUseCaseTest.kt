package com.example.hexagonal.application

import com.example.hexagonal.application.usecase.GetUserUseCase
import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.model.UserId
import com.example.hexagonal.domain.port.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val getUserUseCase = GetUserUseCase(userRepository)

    @Test
    fun `should get user by id successfully`() {
        val user = User.create("John Doe", "john.doe@example.com", 30)
        val request = GetUserUseCase.GetUserRequest(user.id.value)

        every { userRepository.findById(user.id) } returns user

        val result = getUserUseCase.execute(request)

        assertEquals(user.id.value, result.id)
        assertEquals(user.name, result.name)
        assertEquals(user.email, result.email)
        assertEquals(user.age, result.age)
        verify { userRepository.findById(user.id) }
    }

    @Test
    fun `should throw exception when user not found`() {
        val userId = UserId.generate()
        val request = GetUserUseCase.GetUserRequest(userId.value)

        every { userRepository.findById(userId) } returns null

        assertThrows<NoSuchElementException> {
            getUserUseCase.execute(request)
        }

        verify { userRepository.findById(userId) }
    }
}