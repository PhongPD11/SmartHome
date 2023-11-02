package com.example.smartlamp.model

data class ScheduleModel(
    var hour: String,
    var minute: String,
    val repeat: String,
    var button: Boolean
)