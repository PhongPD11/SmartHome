package com.example.smartlamp.model

data class ScheduleModel(
    var hour: Int,
    var min: Int,
    var button: Int,
    val repeat: ArrayList<Int>,
)