package com.example.smartlamp.repository

import com.example.smartlamp.api.ApiInterface
import javax.inject.Inject

class ScheduleRepository@Inject constructor(
    private val apiInterface: ApiInterface,
){
    fun getSchedule(uid: Int) = apiInterface.getSchedule(uid)
//    fun readNotification(id: Int) = apiInterface.readNotification(id)
}