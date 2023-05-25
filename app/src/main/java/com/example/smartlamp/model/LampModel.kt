package com.example.smartlamp.model

data class LampModel(
    var brightness: Float = 0f,
    var schedule: Map<String, Schedule> = HashMap(),
    var state: Int = 0,
    var flicker: Int = 0,
)

data class Schedule(
    var repeat: ArrayList<Int> = arrayListOf(),
    var state: Int =0,
    var time: Time = Time()
)

data class Time(
    var hourOff: Int = 0,
    var hourOn: Int = 0,
    var minOff: Int = 0,
    var minOn: Int = 0
)
