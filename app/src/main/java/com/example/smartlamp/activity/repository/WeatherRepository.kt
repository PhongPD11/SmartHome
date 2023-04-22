package com.example.smartlamp.activity.repository

import androidx.lifecycle.MutableLiveData
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    val apiInterface: ApiInterface
) {
    var weather = MutableLiveData<WeatherModel>()
    private val apikey = "UTvSQMVsY9jObnFYvAGUDX2g7qgrzSfA"
    private val language = "vi"

    fun getWeatherDaily(): MutableLiveData<WeatherModel> {
        apiInterface.getWeather(apikey,language)
            .enqueue(object : Callback<WeatherModel> {
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (response.body() != null) {
                        val data = response.body()
                        weather.postValue(data!!)
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        return weather
    }
}