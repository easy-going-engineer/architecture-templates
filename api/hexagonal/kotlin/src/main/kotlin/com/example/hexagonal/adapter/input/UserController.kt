package com.example.hexagonal.adapter.input

import com.example.hexagonal.application.usecase.CreateUserUseCase
import com.example.hexagonal.application.usecase.GetAllUsersUseCase
import com.example.hexagonal.application.usecase.GetUserUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase
) {

    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        val useCaseRequest = CreateUserUseCase.CreateUserRequest(
            name = request.name,
            email = request.email,
            age = request.age
        )
        
        val response = createUserUseCase.execute(useCaseRequest)
        
        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreateUserResponse(
                id = response.id,
                name = response.name,
                email = response.email,
                age = response.age
            )
        )
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String): ResponseEntity<GetUserResponse> {
        val request = GetUserUseCase.GetUserRequest(id)
        val response = getUserUseCase.execute(request)
        
        return ResponseEntity.ok(
            GetUserResponse(
                id = response.id,
                name = response.name,
                email = response.email,
                age = response.age
            )
        )
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<GetAllUsersResponse> {
        val response = getAllUsersUseCase.execute()
        
        return ResponseEntity.ok(
            GetAllUsersResponse(
                users = response.users.map { user ->
                    UserResponse(
                        id = user.id,
                        name = user.name,
                        email = user.email,
                        age = user.age
                    )
                }
            )
        )
    }

    data class CreateUserRequest(
        val name: String,
        val email: String,
        val age: Int
    )

    data class CreateUserResponse(
        val id: String,
        val name: String,
        val email: String,
        val age: Int
    )

    data class GetUserResponse(
        val id: String,
        val name: String,
        val email: String,
        val age: Int
    )

    data class GetAllUsersResponse(
        val users: List<UserResponse>
    )

    data class UserResponse(
        val id: String,
        val name: String,
        val email: String,
        val age: Int
    )
}