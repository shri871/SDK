package com.example.sdk2

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.LogLevel
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.*

/*class IOSSDKFileAntilog : Antilog() {
    private val logFilePath: String by lazy {
        val paths = NSSearchPathForDirectoriesInDomains(
            NSDocumentDirectory,
            NSUserDomainMask,
            true
        )
        val documentsDir = paths[0] as? String ?: ""
        "$documentsDir/sdk_logs.txt"
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun performLog(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val timestamp = getCurrentTimestamp()
        println("timestamp: $timestamp")

        val fullMessage =
            "$timestamp [${priority.name}] ${tag.orEmpty()} ${message.orEmpty()}\n" +
                    (throwable?.message ?: "")

        val data = (fullMessage as NSString).dataUsingEncoding(NSUTF8StringEncoding)!!
        val fileManager = NSFileManager.defaultManager

        if (fileManager.fileExistsAtPath(logFilePath)) {
            val handle = NSFileHandle.fileHandleForWritingAtPath(logFilePath)
            handle?.seekToEndOfFile()
            handle?.writeData(data)
            handle?.closeFile()
        } else {
            (fullMessage as NSString).writeToFile(
                path = logFilePath,
                atomically = true,
                encoding = NSUTF8StringEncoding,
                error = null
            )
        }
    }

    private fun getCurrentTimestamp(): String {
        val formatter = NSDateFormatter()
        println("Formatter: $formatter")
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        println("date: ${formatter.stringFromDate(NSDate())}")
        return formatter.stringFromDate(NSDate())
    }

}*/


class IOSSDKFileAntilog : Antilog() {
    private val logFile: NSURL by lazy {
        val dir = Logger.getDirectory()
        val filePath = (dir as NSString).stringByAppendingPathComponent("sdk_logs.txt")
        NSURL.fileURLWithPath(filePath)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun performLog(priority: LogLevel, tag: String?, throwable: Throwable?, message: String?) {
        val timestamp = getCurrentTimestamp()
        val log = "$timestamp [${priority.name}] ${tag.orEmpty()} ${message.orEmpty()}\n"

        val handle = NSFileHandle.fileHandleForWritingToURL(logFile, null)
            ?: run {
                NSFileManager.defaultManager.createFileAtPath(logFile.path!!, null, null)
                NSFileHandle.fileHandleForWritingToURL(logFile, null)!!
            }

        handle.seekToEndOfFile()
        handle.writeData((log + (throwable?.toString() ?: "")).encodeToByteArray().toNSData())
        handle.closeFile()
    }

    private fun getCurrentTimestamp(): String {
        val formatter = NSDateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        return formatter.stringFromDate(NSDate())
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    fun ByteArray.toNSData(): NSData =
        this.usePinned {
            NSData.create(bytes = it.addressOf(0), length = this.size.toULong())
        }

}

actual fun createPlatformAntilog(): Antilog = IOSSDKFileAntilog()