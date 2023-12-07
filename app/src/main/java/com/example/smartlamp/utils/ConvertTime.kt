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
    fun formatDate(time: String?, formatTo: String): String {
        return if (time.isNullOrEmpty()) {
            ""
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val outputFormat = SimpleDateFormat(formatTo)
            val date = inputFormat.parse(time)
            outputFormat.format(date)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateExtra(time: String?, formatTo: String): String {
        return if (time.isNullOrEmpty()) {
            ""
        } else {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val outputFormat = SimpleDateFormat(formatTo)
            val date = inputFormat.parse(time)
            // add 3 days
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DAY_OF_MONTH, 3)
            val newDate = calendar.time
            return outputFormat.format(newDate)
        }
    }
}