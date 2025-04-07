package com.sample.weather.domain.repository

import com.sample.weather.repository.WeatherInfo


interface WeatherRepository {
    suspend fun getWeatherByLocation(lat: Double, lon: Double): WeatherInfo
}
