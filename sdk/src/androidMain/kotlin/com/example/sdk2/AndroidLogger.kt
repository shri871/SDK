package com.example.sdk2

import android.content.Context
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AndroidSDKFileAntilog() : Antilog() {

//    private val logFile = File(context.filesDir, "sdk_logs.txt")
    private val logFile = File(Logger.getDirectory(), "sdk_logs.txt")

    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val timestamp = getCurrentTimestamp()

        val log = "$timestamp [${priority.name}] ${tag.orEmpty()} ${message.orEmpty()}\n"
        FileWriter(logFile, true).use {
            it.append(log)
            throwable?.let { t ->
                it.append(t.stackTraceToString() + "\n")
            }
        }
    }

    private fun getCurrentTimestamp(): String {
        val date = Date(Clock.System.now().toEpochMilliseconds())
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())
        return sdf.format(date)
    }
}

actual fun createPlatformAntilog(): Antilog = AndroidSDKFileAntilog()