package com.sample.weather.domain.model.usecase

import com.sample.weather.domain.repository.WeatherRepository
import com.sample.weather.repository.WeatherInfo

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): WeatherInfo {
        return repository.getWeatherByLocation(lat, lon)
    }
}

