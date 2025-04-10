package com.sample.weather.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.weather.domain.model.usecase.GetWeatherUseCase
import com.sample.weather.presentation.state.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
) : ViewModel() {

    private val _weatherState = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weatherState: StateFlow<WeatherState> = _weatherState

    fun fetchWeatherByLocation(lat: Double, lon: Double,defaultError: String) {
        viewModelScope.launch {
            _weatherState.value = WeatherState.Loading
            try {
                val weatherInfo = getWeatherUseCase(lat, lon)
                _weatherState.value = WeatherState.Success(weatherInfo)
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error.Generic(
                    e.message ?: defaultError
                )
            }
        }
    }

    fun setError(message: String) {
        _weatherState.value = WeatherState.Error.Generic(message)
    }

}

