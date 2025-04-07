package com.sample.weather.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.weather.R
import com.sample.weather.domain.model.usecase.GetWeatherUseCase
import com.sample.weather.repository.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _weatherInfo = MutableStateFlow<WeatherInfo?>(null)
    val weatherInfo: StateFlow<WeatherInfo?> = _weatherInfo

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchWeatherByLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = getWeatherUseCase(lat, lon)
                _weatherInfo.value = result
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: context.getString(R.string.something_wrong)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

}

