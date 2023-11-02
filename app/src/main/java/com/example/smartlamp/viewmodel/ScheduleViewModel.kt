package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.model.ScheduleModel
import com.example.smartlamp.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    sharedPref: SharedPref
) : ViewModel() {

    init {

    }

    val scheduleList = MutableLiveData<List<ScheduleModel>>()

}