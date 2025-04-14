package com.sample.weather.core.utils

import android.content.Context
import com.sample.weather.R
import com.sample.weather.data.remote.dto.CurrentWeather
import com.sample.weather.repository.WeatherInfo

object WeatherMapper {

    fun mapToDomain(context: Context, dto: CurrentWeather): WeatherInfo {
        return WeatherInfo(
            temperature = dto.temperature,
            windSpeed = dto.windspeed,
            condition = mapWeatherCode(context, dto.weathercode),
            time = dto.time
        )
    }


    fun mapWeatherCode(context: Context, code: Int): String {
        return when (code) {
            0 -> context.getString(R.string.weather_clear)
            1, 2, 3 -> context.getString(R.string.weather_cloudy)
            45, 48 -> context.getString(R.string.weather_foggy)
            51, 53, 55 -> context.getString(R.string.weather_drizzle)
            61, 63, 65 -> context.getString(R.string.weather_rainy)
            71, 73, 75 -> context.getString(R.string.weather_snowy)
            80, 81, 82 -> context.getString(R.string.weather_showers)
            95 -> context.getString(R.string.weather_thunderstorm)
            96, 99 -> context.getString(R.string.weather_severe_thunderstorm)
            else -> context.getString(R.string.weather_unknown)
        }
    }
}
