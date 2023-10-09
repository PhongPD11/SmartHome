package io.tux.wallet.testnet.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object ConvertTime {
    @RequiresApi(Build.VERSION_CODES.O)
    fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): LocalDateTime? {
        return try {
            LocalDateTime.parse(this, DateTimeFormatter.ofPattern(dateFormat).withZone(timeZone.toZoneId()))
        } catch (e: Exception) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun LocalDateTime.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        return try {
            this.atZone(timeZone.toZoneId()).format(DateTimeFormatter.ofPattern(dateFormat))
        } catch (e: Exception) {
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(time: String, formatTo: String): String {
        val dateTime = time.toDate()
        return dateTime?.formatTo(formatTo) ?: ""
    }
}