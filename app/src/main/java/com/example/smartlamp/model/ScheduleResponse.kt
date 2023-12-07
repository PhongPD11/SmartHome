package com.example.smartlamp.model

import java.io.Serializable

data class ScheduleResponse(
    val code: Int,
    val data: List<ScheduleData>,
    val message: String
) {
    data class ScheduleData(
        val id: Int,
        val uid: Int,
        val hourTime: Int,
        val minuteTime: Int,
        val isOn: Boolean,
        val typeRepeat: String,
        val repeat: String
    ) : Serializable
}


