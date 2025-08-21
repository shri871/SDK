package com.example.sdk2

class Maths {
    private val a = 10
    private val b = 20

    fun add(): Int {
        Logger.i(tag = "Maths_SDK") { "Add function" }
        return a + b
    }

    fun subtract(): Int {
        Logger.i(tag = "Maths_SDK") { "Subtract function" }
        return b - a
    }

    fun multiple(): Int {
        Logger.i(tag = "Maths_SDK") { "Multiply function" }
        return a * b
    }

    fun divide(): Int {
        Logger.i(tag = "Maths_SDK") { "Divide function" }
        return b / a
    }
}