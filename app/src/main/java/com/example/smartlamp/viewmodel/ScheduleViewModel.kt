package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.model.ScheduleResponse
import com.example.smartlamp.repository.ScheduleRepository
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    sharedPref: SharedPref,
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    init {
        val uid = sharedPref.getInt(UID)
        getSchedule(uid)
    }

    val scheduleList = MutableLiveData<List<ScheduleResponse.ScheduleData>>()

    fun getSchedule(uid: Int) {
        scheduleRepository.getSchedule(uid).enqueue(object : Callback<ScheduleResponse> {
            override fun onResponse(call: Call<ScheduleResponse>, response: Response<ScheduleResponse>) {
                scheduleList.value = response.body()?.data
            }

            override fun onFailure(call: Call<ScheduleResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}