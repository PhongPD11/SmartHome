package com.example.smartlamp.model

//data class LampModel(
//    var state: Boolean?,
//    var brightness: Float,
//    val schedule: ArrayList<Schedule> = ArrayList()
//)
//
//data class Schedule(
//    val repeat: ArrayList<Int> = ArrayList(),
//    val time: Time? = null
//)
//
//data class Time(
//    var hourOn: Int = 0,
//    var minOn: Int = 0,
//    var hourOff: Int = 0,
//    var minOff: Int = 0
//)

data class LampModel(
    var brightness: Float = 0f,
    var schedule: HashMap<String, Schedule> = HashMap(),
    var state: Int = 0
)

data class Schedule(
    var repeat: HashMap<String, Int> = HashMap(),
    var state: Int =0,
    var time: Time = Time()
)

data class Time(
    var hourOff: Int = 0,
    var hourOn: Int = 0,
    var minOff: Int = 0,
    var minOn: Int = 0
)
