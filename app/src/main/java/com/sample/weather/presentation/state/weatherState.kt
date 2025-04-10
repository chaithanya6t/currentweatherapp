package com.sample.weather.presentation.state

import com.sample.weather.repository.WeatherInfo

sealed class WeatherState {
    data object Loading : WeatherState()
    data class Success(val data: WeatherInfo) : WeatherState()
    sealed class Error : WeatherState() {
        data class Generic(val message: String) : Error()
    }
}