package com.example.hexagonal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HexagonalApiApplication

fun main(args: Array<String>) {
    runApplication<HexagonalApiApplication>(*args)
}