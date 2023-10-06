package com.example.smartlamp.repository

import com.example.smartlamp.api.ApiInterface
import javax.inject.Inject

class NotificationRepository@Inject constructor(
    private val apiInterface: ApiInterface,
){
    fun getNotify(uid: Int) = apiInterface.getNotification(uid)
}