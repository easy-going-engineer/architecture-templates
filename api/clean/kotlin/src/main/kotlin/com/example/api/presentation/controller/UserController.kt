package com.example.api.presentation.controller

import com.example.api.application.service.UserService
import com.example.api.presentation.dto.CreateUserRequest
import com.example.api.presentation.dto.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return try {
            val user = userService.createUser(request.email, request.name)
            ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
    }
    
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return try {
            val user = userService.getUser(id)
            ResponseEntity.ok(UserResponse.from(user))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.notFound().build()
        }
    }
    
    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userService.getAllUsers()
        val userResponses = users.map { UserResponse.from(it) }
        return ResponseEntity.ok(userResponses)
    }
}