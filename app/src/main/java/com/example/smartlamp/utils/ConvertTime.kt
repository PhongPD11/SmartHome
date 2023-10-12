package com.example.smartlamp.utils


import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object ConvertTime {

    @SuppressLint("SimpleDateFormat")
    fun formatDate(time: String, formatTo: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        val outputFormat = SimpleDateFormat(formatTo)
        val date = inputFormat.parse(time)
        return outputFormat.format(date)
    }
}