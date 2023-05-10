package com.example.smartlamp.model

data class AlarmModel(
    var time: String,
    var button: Boolean,
    val repeat: ArrayList<Int>,
)