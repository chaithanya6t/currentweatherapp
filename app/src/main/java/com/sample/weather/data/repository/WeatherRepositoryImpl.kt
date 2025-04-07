package com.sample.weather.domain.model.usecase


import android.content.Context
import com.sample.weather.data.remote.api.WeatherApi
import com.sample.weather.domain.repository.WeatherRepository
import com.sample.weather.repository.WeatherInfo
import com.sample.weather.utils.WeatherMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    @ApplicationContext private val context: Context
) : WeatherRepository {

    override suspend fun getWeatherByLocation(lat: Double, lon: Double): WeatherInfo {
        val response = api.getWeather(lat, lon)
        return WeatherMapper.mapToDomain(context, response.currentWeather)
    }
}



