package com.example.smartlamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.activity.repository.WeatherRepository
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

//    fun deleteWeather(weather: Weather) = viewModelScope.launch(Dispatchers.IO) {
//        weatherRepository.delete(weather)
//    }
//
//    fun updateWeather(weather: Weather) = viewModelScope.launch(Dispatchers.IO) {
//        weatherRepository.update(weather)
//    }
//
//    fun addWeather(weather: Weather) = viewModelScope.launch(Dispatchers.IO) {
//        weatherRepository.insert(weather)
//    }
}