package com.example.hexagonal.application

import com.example.hexagonal.application.usecase.CreateUserUseCase
import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.port.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CreateUserUseCaseTest {

    private val userRepository = mockk<UserRepository>()
    private val createUserUseCase = CreateUserUseCase(userRepository)

    @Test
    fun `should create user successfully`() {
        val request = CreateUserUseCase.CreateUserRequest(
            name = "John Doe",
            email = "john.doe@example.com",
            age = 30
        )

        val expectedUser = User.create(request.name, request.email, request.age)
        every { userRepository.save(any()) } returns expectedUser

        val result = createUserUseCase.execute(request)

        assertEquals(expectedUser.name, result.name)
        assertEquals(expectedUser.email, result.email)
        assertEquals(expectedUser.age, result.age)
        verify { userRepository.save(any()) }
    }
}