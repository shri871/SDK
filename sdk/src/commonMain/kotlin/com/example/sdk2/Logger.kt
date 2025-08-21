package com.example.sdk2

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel

object Logger {
    private var directory: String? = null
    private var sdkAntilog: Antilog? = null

    fun logInit(dirPath: String) {
        directory = dirPath
        sdkAntilog = createPlatformAntilog()
    }

    fun i(throwable: Throwable? = null, tag: String? = null, message: () -> String) {
        sdkAntilog?.log(LogLevel.INFO, tag, throwable, message())
            ?: error("SDK Logger not initialized") // fallback
    }

    internal fun getDirectory(): String =
        directory ?: error("Logger directory not set. Call logInit() first.")
}

expect fun createPlatformAntilog(): Antilog