package com.example.hexagonal.domain.model

import java.util.*

@JvmInline
value class UserId(val value: String) {
    companion object {
        fun generate(): UserId = UserId(UUID.randomUUID().toString())
        fun of(value: String): UserId = UserId(value)
    }
}