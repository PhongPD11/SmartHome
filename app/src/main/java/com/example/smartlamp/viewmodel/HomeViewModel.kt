package com.example.smartlamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.repository.WeatherRepository
import com.example.smartlamp.model.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    val weather: LiveData<WeatherModel>
        get() = weatherRepository.weather


    init {
        weatherRepository.getWeatherDaily()
    }
}