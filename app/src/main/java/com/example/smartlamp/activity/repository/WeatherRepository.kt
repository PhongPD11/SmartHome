package com.example.smartlamp.activity.repository

import androidx.lifecycle.MutableLiveData
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.model.WeatherModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiInterface: ApiInterface,
//    private val weatherDao: WeatherDao
){

//    val localWeather: LiveData<List<Weather>> = weatherDao.getAllWeathers()
    var weather = MutableLiveData<WeatherModel>()
    private val apikey = "UTvSQMVsY9jObnFYvAGUDX2g7qgrzSfA"
    private val language = "vi"
    private val locationKey = "353981"

    fun getWeatherDaily() {
        try {
            apiInterface.getWeather(apikey,language)
                .enqueue(object : Callback<WeatherModel> {
                    override fun onResponse(
                        call: Call<WeatherModel>,
                        response: Response<WeatherModel>
                    ) {
                        if (response.body() != null) {
                            val data = response.body()
                            weather.postValue(data!!)
                            println(data.headline.text)
                        }
                    }

                    override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        } catch (e: java.lang.Exception){
            println("Ngu nhu cho" + e.message)
        }
    }

//    suspend fun insert(weather: Weather) {
//        weatherDao.insert(weather)
//    }
//
//    suspend fun delete(weather: Weather) {
//        weatherDao.delete(weather)
//    }
//
//    suspend fun update(weather: Weather) {
//        weatherDao.update(weather)
//    }
}

