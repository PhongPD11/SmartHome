package com.example.smartlamp.model

data class ScheduleModel(
    var hour: Int,
    var minute: Int,
    val repeat: String,
    var button: Boolean
)


