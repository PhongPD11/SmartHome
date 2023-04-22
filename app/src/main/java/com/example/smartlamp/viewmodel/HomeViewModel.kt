package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.activity.repository.WeatherRepository
import com.example.smartlamp.model.WeatherModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {
    init {
        test()
//        getWeatherDaily()
    }

    private val _data = MutableLiveData<WeatherModel>()
    val data: MutableLiveData<WeatherModel> = _data

    private val _temp = MutableLiveData<Double>()
    val temp: MutableLiveData<Double> = _temp


    fun test() : Boolean{
        _temp.value = 2.0
        return true
    }

//    private val weatherData = repository.getWeatherDaily()
//    fun getWeatherDaily(): MutableLiveData<WeatherModel> {
//        _data.value = repository.getWeatherDaily().value
//        return weatherData
//    }
//
//    fun setUp(){
//        _temp.value = _data.value?.DailyForecasts?.Temperature?.Minimum?.Value
//    }

//    private val _photos = MutableLiveData<List<CntInternetPhoto>>()
//    val photos: LiveData<List<CntInternetPhoto>> = _photos

}