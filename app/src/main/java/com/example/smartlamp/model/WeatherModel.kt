package com.example.smartlamp.model

data class WeatherModel(
    val Headline : HeadlineData,
    val DailyForecasts: DailyData,
)

data class  HeadlineData(
    val EffectiveDate: String,
    val EffectiveEpochDate: Int,
    val EndDate: String,
    val EndEpochDate: Int,

    val Severity: Int,
    val Text: String,
    val Category: String,
)

data class  DailyData (
    val Date: String,
    val EpochDate: Int,
    val Temperature : TemperatureData,
    val Day: Status,
    val Night: Status,
)

data class Status(
    val IconPhrase: String,
    val HasPrecipitation: Boolean,
    val PrecipitationType: String,
    val PrecipitationIntensity: String,
)


data class TemperatureData(
    val Minimum: Temp,
    val Maximum: Temp,
)

data class Temp(
    val Value: Double,
    val Unit: String,
    val UnitType: Int
)