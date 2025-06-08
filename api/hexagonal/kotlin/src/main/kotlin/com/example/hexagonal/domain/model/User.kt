package com.example.hexagonal.domain.model

data class User(
    val id: UserId,
    val name: String,
    val email: String,
    val age: Int
) {
    companion object {
        fun create(name: String, email: String, age: Int): User {
            validateName(name)
            validateEmail(email)
            validateAge(age)
            
            return User(
                id = UserId.generate(),
                name = name,
                email = email,
                age = age
            )
        }

        private fun validateName(name: String) {
            require(name.isNotBlank()) { "Name cannot be empty" }
        }

        private fun validateEmail(email: String) {
            val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
            require(email.matches(emailRegex)) { "Invalid email format" }
        }

        private fun validateAge(age: Int) {
            require(age >= 0) { "Age cannot be negative" }
        }
    }

    fun updateName(newName: String): User {
        User.validateName(newName)
        return copy(name = newName)
    }

    fun updateEmail(newEmail: String): User {
        User.validateEmail(newEmail)
        return copy(email = newEmail)
    }

    fun updateAge(newAge: Int): User {
        User.validateAge(newAge)
        return copy(age = newAge)
    }
}