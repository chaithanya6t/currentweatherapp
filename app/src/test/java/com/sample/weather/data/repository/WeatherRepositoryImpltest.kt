package com.sample.weather.data.repository


import android.content.Context
import com.sample.weather.data.remote.api.WeatherApi
import com.sample.weather.data.remote.dto.CurrentWeather
import com.sample.weather.data.remote.dto.WeatherResponse
import com.sample.weather.domain.model.usecase.WeatherRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherRepositoryImplTest {

    private val api = mockk<WeatherApi>()
    private val context = mockk<Context>(relaxed = true)
    private lateinit var repository: WeatherRepositoryImpl

    @Before
    fun setup() {
        repository = WeatherRepositoryImpl(api, context)
    }

    @Test
    fun `getWeatherByLocation returns mapped weather info`() = runTest {
        val response = WeatherResponse(
            latitude = 28.6,
            longitude = 77.2,
            currentWeather = CurrentWeather(25.5, 5.2, 0, "2025-04-07T10:00")
        )

        coEvery { api.getWeather(any(), any(), any()) } returns response

        val result = repository.getWeatherByLocation(28.6, 77.2)

        assertEquals(25.5, result.temperature, 0.01)
        assertEquals(5.2, result.windSpeed, 0.01)
        assertEquals("Clear sky", result.condition)
    }
}



