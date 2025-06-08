package com.example.api.presentation.controller

import com.example.api.application.service.UserService
import com.example.api.domain.model.User
import com.example.api.presentation.dto.CreateUserRequest
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create user successfully`() {
        val request = CreateUserRequest("test@example.com", "Test User")
        val user = User(1L, "test@example.com", "Test User", LocalDateTime.now(), LocalDateTime.now())

        every { userService.createUser(request.email, request.name) } returns user

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.name").value("Test User"))
    }

    @Test
    fun `should return bad request when user creation fails`() {
        val request = CreateUserRequest("test@example.com", "Test User")

        every { userService.createUser(request.email, request.name) } throws IllegalArgumentException("Email already exists")

        mockMvc.perform(
            post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should get user by id successfully`() {
        val user = User(1L, "test@example.com", "Test User", LocalDateTime.now(), LocalDateTime.now())

        every { userService.getUser(1L) } returns user

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.name").value("Test User"))
    }

    @Test
    fun `should return not found when user does not exist`() {
        every { userService.getUser(1L) } throws IllegalArgumentException("User not found")

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get all users successfully`() {
        val users = listOf(
            User(1L, "test1@example.com", "Test User 1", LocalDateTime.now(), LocalDateTime.now()),
            User(2L, "test2@example.com", "Test User 2", LocalDateTime.now(), LocalDateTime.now())
        )

        every { userService.getAllUsers() } returns users

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
    }
}