package com.example.smartlamp.api

import com.example.smartlamp.model.WeatherModel
import com.example.smartlamp.utils.Urls.GET_WEATHER_FORECAST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface
ApiInterface {
    @GET("$GET_WEATHER_FORECAST/{location}")
    fun getWeather(
        @Path("location") location: String,
        @Query("apikey") apiKey: String,
        @Query("language") language: String
    ): Call<WeatherModel>
}


