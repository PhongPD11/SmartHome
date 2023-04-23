package com.example.smartlamp.model

import com.google.gson.annotations.SerializedName


data class WeatherModel(
    @SerializedName("Headline") val headline: Headline,
    @SerializedName("DailyForecasts") val dailyForecasts: List<DailyForecast>
)

data class Headline(
    @SerializedName("Text") val text: String
)

data class DailyForecast(
    @SerializedName("Date") val date: String,
    @SerializedName("Temperature") val temperature: Temperature,
    @SerializedName("Day") val day: Status,
    @SerializedName("Night") val night: Status,
)

data class Temperature(
    @SerializedName("Minimum") val minimum: Temp,
    @SerializedName("Maximum") val maximum: Temp
)

data class Status(
    @SerializedName("IconPhrase") val icon : String,
    @SerializedName("HasPrecipitation") val hasPrecipitation : Boolean,
)

data class Temp(
    @SerializedName("Value") val value: Double,
    @SerializedName("Unit") val unit: String
)