package com.example.hexagonal.domain

import com.example.hexagonal.domain.model.User
import com.example.hexagonal.domain.model.UserId
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserTest {

    @Test
    fun `should create user with valid data`() {
        val user = User.create(
            name = "John Doe",
            email = "john.doe@example.com",
            age = 30
        )

        assertNotNull(user.id)
        assertEquals("John Doe", user.name)
        assertEquals("john.doe@example.com", user.email)
        assertEquals(30, user.age)
    }

    @Test
    fun `should throw exception when name is empty`() {
        assertThrows<IllegalArgumentException> {
            User.create(
                name = "",
                email = "john.doe@example.com",
                age = 30
            )
        }
    }

    @Test
    fun `should throw exception when email is invalid`() {
        assertThrows<IllegalArgumentException> {
            User.create(
                name = "John Doe",
                email = "invalid-email",
                age = 30
            )
        }
    }

    @Test
    fun `should throw exception when age is negative`() {
        assertThrows<IllegalArgumentException> {
            User.create(
                name = "John Doe",
                email = "john.doe@example.com",
                age = -1
            )
        }
    }

    @Test
    fun `should update user name`() {
        val user = User.create(
            name = "John Doe",
            email = "john.doe@example.com",
            age = 30
        )

        val updatedUser = user.updateName("Jane Doe")

        assertEquals("Jane Doe", updatedUser.name)
        assertEquals(user.id, updatedUser.id)
        assertEquals(user.email, updatedUser.email)
        assertEquals(user.age, updatedUser.age)
    }
}