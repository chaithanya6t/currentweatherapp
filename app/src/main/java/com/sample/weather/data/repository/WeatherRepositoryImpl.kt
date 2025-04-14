package com.sample.weather.domain.model.usecase


import android.content.Context
import com.sample.weather.R
import com.sample.weather.data.remote.api.WeatherApi
import com.sample.weather.domain.repository.WeatherRepository
import com.sample.weather.repository.WeatherInfo
import com.sample.weather.core.utils.WeatherMapper
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    @ApplicationContext private val context: Context
) : WeatherRepository {

    override suspend fun getWeatherByLocation(lat: Double, lon: Double): WeatherInfo {

        return try {
            val response = api.getWeather(lat, lon)
            WeatherMapper.mapToDomain(context, response.currentWeather)

        }catch (e: IOException) {
            throw IOException(context.getString(R.string.network_error_occurred), e)
        } catch (e: HttpException) {
            throw Exception( context.getString(R.string.server_error_occurred, e.code(), e.message()), e)
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.unexpected_error_occurred, e.message ?: context.getString(R.string.something_wrong)), e)
        }
        }
}



