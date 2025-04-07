package com.sample.weather.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("current_weather") val currentWeather: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature") val temperature: Double,
    @SerializedName("windspeed") val windspeed: Double,
    @SerializedName("weathercode") val weathercode: Int,
    @SerializedName("time") val time: String
)
