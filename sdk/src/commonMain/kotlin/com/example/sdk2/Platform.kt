package com.example.sdk2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform