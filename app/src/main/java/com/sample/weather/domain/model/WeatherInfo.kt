package com.sample.weather.repository

data class WeatherInfo(
    val temperature: Double,
    val windSpeed: Double,
    val condition: String,
    val time: String
)
